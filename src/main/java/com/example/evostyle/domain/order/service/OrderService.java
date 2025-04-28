package com.example.evostyle.domain.order.service;

import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.CreateOrderItemResponse;
import com.example.evostyle.domain.order.dto.response.CreateOrderItemWrapper;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderItemRepository;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public CreateOrderItemWrapper createOrder(List<CreateOrderItemRequest> requestList) {

        List<Long> productIdList = requestList.stream()
                .map(CreateOrderItemRequest::productId)
                .toList();

        List<Product> productList = productRepository.findByIdList(productIdList);

        Map<Long, Product> productIdToProduct = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        if (productIdList.size() != productIdToProduct.size()) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // todo 인증/인가 적용 후 수정 예정
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        int totalAmountSum = 0;
        int totalPriceSum = 0;

        Order order = Order.of(member, totalAmountSum, totalPriceSum);

        List<OrderItem> orderItemList = new ArrayList<>();
        List<CreateOrderItemResponse> orderItemResponseList = new ArrayList<>();

        for (CreateOrderItemRequest request : requestList) {
            Product product = productIdToProduct.get(request.productId());
            totalAmountSum += request.eachAmount();

            int totalPrice = product.getPrice() * request.eachAmount();
            totalPriceSum += totalPrice;

            OrderItem orderItem = OrderItem.of(
                    request.eachAmount(),
                    totalPrice,
                    order,
                    OrderStatus.PENDING,
                    product.getName(),
                    product.getPrice(),
                    product.getDescription()
            );

            orderItemList.add(orderItem);

            CreateOrderItemResponse response = CreateOrderItemResponse.from(
                    orderItem,
                    product.getId()
            );

            orderItemResponseList.add(response);
        }

        orderRepository.save(order);

        orderItemRepository.saveAll(orderItemList);

        return CreateOrderItemWrapper.from(
                order.getId(),
                orderItemResponseList,
                totalAmountSum,
                totalPriceSum
        );
    }
}
