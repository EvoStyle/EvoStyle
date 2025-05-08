package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;

public record GuestCartItemResponse(
        ProductDetailResponse productDetailResponse,
        Integer quantity,
        Integer originTotalPrice
) {

    public static GuestCartItemResponse of(Integer productPrice,
                                           ProductDetailResponse productDetailResponse,
                                           RedisCartItemDto redisCartItemDto) {

        int originTotalPrice = productPrice * redisCartItemDto.getQuantity() ;

        return new GuestCartItemResponse(
                productDetailResponse,
                redisCartItemDto.getQuantity(),
                originTotalPrice
        );
    }
}
