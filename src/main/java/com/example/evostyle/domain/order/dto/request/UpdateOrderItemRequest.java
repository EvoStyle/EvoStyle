package com.example.evostyle.domain.order.dto.request;

public record UpdateOrderItemRequest(
        Long productDetailId,
        Integer newAmount
) {
}
