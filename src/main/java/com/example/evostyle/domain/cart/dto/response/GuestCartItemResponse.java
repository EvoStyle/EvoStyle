package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.common.util.MemberDiscountUtil;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.dto.response.ProductResponse;

public record GuestCartItemResponse(
        ProductResponse productResponse,
        ProductDetailResponse productDetailResponse,
        Integer quantity,
        Integer originPrice
) {

    public static GuestCartItemResponse of(ProductResponse productResponse,
                                           ProductDetailResponse productDetailResponse,
                                           RedisCartItemDto redisCartItemDto) {

        int originPrice = productResponse.price() * redisCartItemDto.getQuantity() ;

        return new GuestCartItemResponse(productResponse,
                productDetailResponse,
                redisCartItemDto.getQuantity(),
                originPrice
        );
    }
}
