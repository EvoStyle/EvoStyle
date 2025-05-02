package com.example.evostyle.domain.order.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.request.UpdateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.*;
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

        List<Long> productDetailIdList = requestList.stream()
                .map(CreateOrderItemRequest::productDetailId)
                .toList();

        List<ProductDetail> productDetailList = productDetailRepository.findAllById(productDetailIdList);

        if (productDetailIdList.size() != productDetailList.size()) {
            throw new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }

        Map<Long, ProductDetail> idToProductDetail = productDetailList.stream()
                .collect(Collectors.toMap(ProductDetail::getId, productDetail -> productDetail));

        // todo 인증/인가 적용 후 수정 예정
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        int totalAmountSum = 0;
        int totalPriceSum = 0;

        Order order = Order.of(
                member,
                totalAmountSum,
                totalPriceSum
        );

        List<OrderItem> orderItemList = new ArrayList<>();

        for (CreateOrderItemRequest request : requestList) {
            ProductDetail productDetail = idToProductDetail.get(request.productDetailId());
            Product product = productDetail.getProduct();
            Brand brand = product.getBrand();
            totalAmountSum += request.eachAmount();

            int totalPrice = product.getPrice() * request.eachAmount();
            totalPriceSum += totalPrice;

            OrderItem orderItem = OrderItem.of(
                    request.eachAmount(),
                    totalPrice,
                    order,
                    brand,
                    OrderStatus.PENDING,
                    productDetail,
                    product.getName(),
                    product.getPrice(),
                    product.getDescription()
            );

            orderItemList.add(orderItem);

            order.addOrderItem(orderItem);
        }

        order.updateAmountAndPrice(totalAmountSum, totalPriceSum);

        orderRepository.save(order);

        List<CreateOrderItemResponse> responseList = orderItemList.stream()
                .map(CreateOrderItemResponse::from)
                .toList();

        return CreateOrderResponse.from(
                order.getId(),
                responseList,
                totalAmountSum,
                totalPriceSum
        );
    }

    public List<ReadOrderResponse> readAllOrders() {

        // todo 인증/인가 적용 후 수정 예정
        memberRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 사장님이 소유한 브랜드 ID 목록 조회
        List<Long> brandIdList = brandRepository.findBrandIdsByMemberId(1L);

        // 해당 브랜드들의 주문 세부 사항 조회
        List<OrderItem> orderItemList = orderItemRepository.findOrderItemsByBrandIdList(brandIdList);

        Map<Long, List<OrderItem>> idToOrderItemList = orderItemList.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getId()));

        List<Long> orderIdList = new ArrayList<>(idToOrderItemList.keySet());

        List<ReadOrderResponse> orderResponseList = new ArrayList<>();

        orderResponseList = orderIdList.stream()
                .map(orderId -> {

                    List<ReadOrderItemResponse> readOrderItemResponseList = idToOrderItemList.get(orderId)
                            .stream()
                            .map(ReadOrderItemResponse::from).toList();

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

    @Transactional
    public UpdateOrderItemResponse updateOrderItem(
            UpdateOrderItemRequest request,
            Long orderId,
            Long orderItemId
    ) {
        OrderItem orderItem = findOrderItemById(orderItemId);

        orderItem.validateOrderIdMatch(orderId);

        Order order = orderItem.getOrder();

        orderItem.validateProductDetailIdMatch(request.productDetailId());

        ProductDetail productDetail = orderItem.getProductDetail();

        // 이전 수량만큼 재고 복원
        int previousAmount = orderItem.getEachAmount();
        int newAmount = request.newAmount();
        int previousTotalPrice = orderItem.getTotalPrice();

        productDetail.adjustStock(previousAmount, newAmount);

        // 새로운 수량 업데이트
        orderItem.update(newAmount);

        // Order 총 수량 및 총 가격 갱신
        int newTotalAmountSum = order.getTotalAmountSum() - previousAmount + newAmount;
        int newTotalPriceSum = order.getTotalPriceSum() - previousTotalPrice + (orderItem.getProductPrice() * newAmount);

        order.updateAmountAndPrice(newTotalAmountSum, newTotalPriceSum);

        return UpdateOrderItemResponse.from(orderItem);
    }

    @Transactional
    public void deleteOrderItem(
            Long orderId,
            Long orderItemId
    ) {
        OrderItem orderItem = findOrderItemById(orderItemId);

        orderItem.validateOrderIdMatch(orderId);

        Order order = orderItem.getOrder();

        int updatedTotalAmountSum = order.getTotalAmountSum() - orderItem.getEachAmount();
        int updatedTotalPriceSum = order.getTotalPriceSum() - orderItem.getTotalPrice();

        order.updateAmountAndPrice(updatedTotalAmountSum, updatedTotalPriceSum);

        orderItem.markAsCancelled();

        if (!orderItemRepository.existsByOrderIdAndIsCancelledFalse(orderId)) {
            order.markAsCancelled();
        }
    }

    private OrderItem findOrderItemById(Long orderItemId) {
        return orderItemRepository.findByIdAndOrderStatus(orderItemId, OrderStatus.PENDING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_ITEM_NOT_PENDING));
    }
}