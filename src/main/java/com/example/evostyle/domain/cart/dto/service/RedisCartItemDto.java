package com.example.evostyle.domain.cart.dto.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisCartItemDto {
    private Long productDetailId;
    private Integer quantity;

    public static RedisCartItemDto of(Long productDetailId, Integer quantity) {
        return new RedisCartItemDto(productDetailId, quantity);
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
