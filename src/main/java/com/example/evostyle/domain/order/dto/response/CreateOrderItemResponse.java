package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;

public record CreateOrderItemResponse(
        Long orderItemId,
        Long productDetailId,
        String productName,
        Integer eachAmount,
        Integer totalPrice,
        OrderStatus orderStatus
) {
    public static CreateOrderItemResponse from(OrderItem orderItem) {
        return new CreateOrderItemResponse(
                orderItem.getId(),
                orderItem.getProductDetail().getId(),
                orderItem.getProductName(),
                orderItem.getEachAmount(),
                orderItem.getTotalPrice(),
                OrderStatus.PENDING
        );
    }
}