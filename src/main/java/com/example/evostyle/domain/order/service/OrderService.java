package com.example.evostyle.domain.order.service;

import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.CreateOrderItemResponse;
import com.example.evostyle.domain.order.dto.response.CreateOrderResponse;
import com.example.evostyle.domain.order.dto.response.ReadOrderItemResponse;
import com.example.evostyle.domain.order.dto.response.ReadOrderResponse;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderItemRepository;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final ProductDetailRepository productDetailRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public CreateOrderResponse createOrder(List<CreateOrderItemRequest> requestList) {

        List<Long> productDetailIdList = requestList.stream().map(CreateOrderItemRequest::productDetailId).toList();

        List<ProductDetail> productDetailList = productDetailRepository.findAllById(productDetailIdList);

        if (productDetailIdList.size() != productDetailList.size()) {
            throw new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }

        Map<Long, ProductDetail> idToProductDetail = productDetailList.stream().collect(Collectors.toMap(ProductDetail::getId, productDetail -> productDetail));

        // todo 인증/인가 적용 후 수정 예정
        Member member = memberRepository.findById(1L).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        int totalAmountSum = 0;
        int totalPriceSum = 0;

        Order order = Order.of(member, totalAmountSum, totalPriceSum);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (CreateOrderItemRequest request : requestList) {
            ProductDetail productDetail = idToProductDetail.get(request.productDetailId());
            Product product = productDetail.getProduct();
            totalAmountSum += request.eachAmount();

            int totalPrice = product.getPrice() * request.eachAmount();
            totalPriceSum += totalPrice;

//            // 재고 차감
            productDetail.decreaseStock(request.eachAmount());

            OrderItem orderItem = OrderItem.of(request.eachAmount(), totalPrice, order, OrderStatus.PENDING, productDetail, product.getName(), product.getPrice(), product.getDescription());

            orderItemList.add(orderItem);
        }

        orderRepository.save(order);

        orderItemRepository.saveAll(orderItemList);

        List<CreateOrderItemResponse> responseList = orderItemList.stream().map(orderItem -> CreateOrderItemResponse.from(orderItem, orderItem.getProductDetail().getId())).toList();

        return CreateOrderResponse.from(order.getId(), responseList, totalAmountSum, totalPriceSum);
    }

    public List<ReadOrderResponse> readAllOrders() {

        // todo 인증/인가 적용 후 수정 예정
        memberRepository.findById(1L).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 사장님이 소유한 브랜드 ID 목록 조회
        List<Long> brandIdList = brandRepository.findBrandIdsByMemberId(1L);

        // 해당 브랜드들의 주문 세부 사항 조회
        List<OrderItem> orderItemList = orderItemRepository.findOrderItemsByBrandIdList(brandIdList);

        Map<Long, List<OrderItem>> idToOrderItemList = orderItemList.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getId()));

        List<Long> orderIdList = new ArrayList<>(idToOrderItemList.keySet());

        List<ReadOrderResponse> orderResponseList = new ArrayList<>();

        orderResponseList = orderIdList.stream().map(orderId -> {

            List<ReadOrderItemResponse> readOrderItemResponseList = idToOrderItemList.get(orderId)
                    .stream()
                    .map(orderItem -> ReadOrderItemResponse.from(
                            orderItem,
                            orderItem.getProductDetail().getId())
                    ).toList();

            int totalAmountSum = readOrderItemResponseList.stream()
                    .mapToInt(ReadOrderItemResponse::eachAmount)
                    .sum();

            int totalPriceSum = readOrderItemResponseList.stream()
                    .mapToInt(ReadOrderItemResponse::totalPrice)
                    .sum();

            return ReadOrderResponse.from(
                    orderId,
                    readOrderItemResponseList,
                    totalAmountSum,
                    totalPriceSum
            );
        }).toList();

        return orderResponseList;
    }
}
