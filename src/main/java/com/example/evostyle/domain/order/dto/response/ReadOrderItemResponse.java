package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;

public record ReadOrderItemResponse(
        Long orderItemId,
        Long brandId,
        Long productDetailId,
        String productName,
        Integer eachAmount,
        Integer totalPrice,
        OrderStatus orderStatus
) {
    public static ReadOrderItemResponse from(OrderItem orderItem) {
        return new ReadOrderItemResponse(
                orderItem.getId(),
                orderItem.getProductDetail().getProduct().getBrand().getId(),
                orderItem.getProductDetail().getId(),
                orderItem.getProductName(),
                orderItem.getEachAmount(),
                orderItem.getTotalPrice(),
                orderItem.getOrderStatus()
        );
    }
}