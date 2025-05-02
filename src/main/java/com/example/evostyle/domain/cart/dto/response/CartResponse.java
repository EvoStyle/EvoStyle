package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.domain.cart.entity.Cart;
import jakarta.annotation.Nullable;

import java.util.List;

public record CartResponse(
        @Nullable Long id,
        List<CartItemResponse> cartItemResponseList,
        Integer totalQuantity,
        Integer totalPrice
) {

    public static CartResponse of( Cart cart, List<CartItemResponse> cartItemResponseList){
        int totalQuantity = cartItemResponseList.stream().mapToInt(CartItemResponse::quantity).sum();
        int totalPrice = cartItemResponseList.stream().mapToInt(CartItemResponse::price).sum();

        return new CartResponse(cart.getId(), cartItemResponseList,
                                totalQuantity, totalPrice);
    }

    public static CartResponse of (List<CartItemResponse> cartItemResponseList) {
        int totalQuantity = cartItemResponseList.stream().mapToInt(CartItemResponse::quantity).sum();
        int totalPrice = cartItemResponseList.stream().mapToInt(CartItemResponse::price).sum();

        return new CartResponse(null, cartItemResponseList, totalQuantity, totalPrice);
    }
}
