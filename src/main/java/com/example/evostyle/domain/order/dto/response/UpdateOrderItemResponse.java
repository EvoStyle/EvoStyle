package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.OrderItem;

public record UpdateOrderItemResponse(
        Long orderItemId,
        Long productDetailId,
        Integer eachAmount,
        Integer totalPrice
) {
    public static UpdateOrderItemResponse from(OrderItem orderItem) {
        return new UpdateOrderItemResponse(
                orderItem.getId(),
                orderItem.getProductDetail().getId(),
                orderItem.getEachAmount(),
                orderItem.getTotalPrice()
        );
    }
}