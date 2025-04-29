package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.OrderItem;

public record ReadOrderItemResponse(
        Long orderItemId,
        Long brandId,
        Long productDetailId,
        String productName,
        Integer eachAmount,
        Integer totalPrice
) {
    public static ReadOrderItemResponse from(
            OrderItem orderItem,
            Long productDetailId
    ) {
        return new ReadOrderItemResponse(
                orderItem.getId(),
                orderItem.getProductDetail().getProduct().getBrand().getId(),
                productDetailId,
                orderItem.getProductName(),
                orderItem.getEachAmount(),
                orderItem.getTotalPrice()
        );
    }
}