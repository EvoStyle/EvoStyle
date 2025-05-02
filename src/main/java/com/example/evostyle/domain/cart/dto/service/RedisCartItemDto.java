package com.example.evostyle.domain.cart.dto.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RedisCartItemDto {
    private Long productDetailId;
    private Integer quantity;
    private Integer price;

    public static RedisCartItemDto of(Long productDetailId, Integer quantity, Integer price) {
        return new RedisCartItemDto(productDetailId, quantity, price);
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
