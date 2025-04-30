package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.OrderItem;

public record CreateOrderItemResponse(
        Long orderItemId,
        Long productDetailId,
        String productName,
        Integer eachAmount,
        Integer totalPrice
) {
    public static CreateOrderItemResponse from(OrderItem orderItem) {
        return new CreateOrderItemResponse(
                orderItem.getId(),
                orderItem.getProductDetail().getId(),
                orderItem.getProductName(),
                orderItem.getEachAmount(),
                orderItem.getTotalPrice()
        );
    }
}