package com.example.evostyle.domain.order.service;

import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.request.UpdateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.CreateOrderItemResponse;
import com.example.evostyle.domain.order.dto.response.CreateOrderResponse;
import com.example.evostyle.domain.order.dto.response.UpdateOrderItemResponse;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.repository.OrderItemQueryDsl;
import com.example.evostyle.domain.order.repository.OrderItemRepository;
import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemQueryDsl orderItemQueryDsl;
    private final OrderService orderService;

    @Transactional
    public CreateOrderResponse createOrderItems(List<CreateOrderItemRequest> requestList, Long memberId) {
        Pair<Order, Map<Long, ProductDetail>> orderMapPair = orderService.createOrder(memberId, requestList);

        Order order = orderMapPair.getFirst();
        Map<Long, ProductDetail> idToProductDetail = orderMapPair.getSecond();

        List<OrderItem> orderItemList = new ArrayList<>();

        for (CreateOrderItemRequest request : requestList) {
            ProductDetail productDetail = idToProductDetail.get(request.productDetailId());

            OrderItem orderItem = OrderItem.of(request.eachAmount(), order, productDetail);

            orderItemList.add(orderItem);

            order.addOrderItem(orderItem);
        }

        orderItemRepository.saveAll(orderItemList);

        List<CreateOrderItemResponse> responseList = orderItemList.stream()
                .map(CreateOrderItemResponse::from)
                .toList();

        return CreateOrderResponse.from(order, responseList);
    }

    @Transactional
    public UpdateOrderItemResponse updateOrderItem(UpdateOrderItemRequest request, Long orderId, Long orderItemId, Long memberId) {
        OrderItem orderItem = findOrderItemById(orderItemId);

        orderItem.validateOrderIdMatch(orderId); // 주문상태 검증 로직 추가

        orderItem.validateProductDetailIdMatch(request.productDetailId());

        orderService.updateOrder(orderItem, memberId, request.newAmount());

        orderItem.update(request.newAmount());

        return UpdateOrderItemResponse.from(orderItem);
    }

    @Transactional
    public void deleteOrderItem(Long orderId, Long orderItemId, Long memberId) {
        OrderItem orderItem = findOrderItemById(orderItemId);

        orderItem.validateOrderIdMatch(orderId);

        orderItem.markAsCancelled();

        orderService.adjustOrderAfterItemCancellation(orderItem, memberId);
    }

    private OrderItem findOrderItemById(Long orderItemId) {
        return orderItemQueryDsl.findPendingById(orderItemId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_ITEM_NOT_PENDING));
    }
}