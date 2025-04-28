package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.OrderItem;

public record CreateOrderItemResponse(
//        Long orderItemId,
        Long productId,
        String productName,
        Integer eachAmount,
        Integer totalPrice
) {
    public static CreateOrderItemResponse from(
            OrderItem orderItem,
            Long productId
    ) {
        return new CreateOrderItemResponse(
//                orderItem.getId(),
                productId,
                orderItem.getProductName(),
                orderItem.getEachAmount(),
                orderItem.getTotalPrice()
        );
    }
}