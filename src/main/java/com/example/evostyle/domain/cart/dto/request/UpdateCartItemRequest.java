package com.example.evostyle.domain.cart.dto.request;


import jakarta.annotation.Nullable;

public record UpdateCartItemRequest(@Nullable Long productDetailId, Integer quantity) {
}
