package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.cart.entity.CartItem;
import jakarta.annotation.Nullable;

public record CartItemResponse(
        @Nullable  Long id,
        @Nullable Long cartId,
        Long productId,
        Long productDetailId,
        Integer quantity,
        Integer price
) {

    public static CartItemResponse from(CartItem cartItem) {

        int price = cartItem.getQuantity() * cartItem.getProductDetail().getProduct().getPrice();

        return new CartItemResponse(cartItem.getId(),
                cartItem.getCart().getId(),
                cartItem.getProductDetail().getProduct().getId(),
                cartItem.getProductDetail().getId(),
                cartItem.getQuantity(),
                price);

    }

    // nullable로 두지 말고 비회원용 객체를 따로 파는게 좋을까요
    public static CartItemResponse from(RedisCartItemDto redisCartItemDto){

        return new CartItemResponse(null,
                                  null,
                                        1L,
                                        redisCartItemDto.getProductDetailId(),
                                        redisCartItemDto.getQuantity(),
                                        redisCartItemDto.getPrice());
    }

}
