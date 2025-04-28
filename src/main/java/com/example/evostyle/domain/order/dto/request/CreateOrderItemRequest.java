package com.example.evostyle.domain.order.dto.request;

public record CreateOrderItemRequest(
        Long productId,
        Integer eachAmount
) {
}
