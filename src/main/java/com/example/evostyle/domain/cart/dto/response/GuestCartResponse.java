package com.example.evostyle.domain.cart.dto.response;

import java.util.List;

public record GuestCartResponse(
        List<GuestCartItemResponse> cartItemResponseList,
        Integer totalQuantity,
        Integer totalPrice
) {


    public static GuestCartResponse from (List<GuestCartItemResponse> cartItemResponseList) {
        int totalQuantity = cartItemResponseList.stream().mapToInt(GuestCartItemResponse::quantity).sum();
        int totalPrice = cartItemResponseList.stream().mapToInt(GuestCartItemResponse::originTotalPrice).sum();

        return new GuestCartResponse(
                cartItemResponseList,
                totalQuantity,
                totalPrice);
    }
}
