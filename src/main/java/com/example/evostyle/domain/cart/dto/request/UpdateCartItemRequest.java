package com.example.evostyle.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(Long productDetailId, Integer quantity) {
}
