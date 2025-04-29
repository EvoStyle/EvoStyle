package com.example.evostyle.domain.order.dto.request;

public record CreateOrderItemRequest(
        Long productDetailId,
        Integer eachAmount
) {
}
