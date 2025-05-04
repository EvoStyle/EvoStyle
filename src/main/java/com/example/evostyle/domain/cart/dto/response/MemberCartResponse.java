package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.common.util.MemberDiscountUtil;
import com.example.evostyle.domain.cart.entity.Cart;
import jakarta.annotation.Nullable;

import java.util.List;

public record MemberCartResponse(
        Long id,
        List<MemberCartItemResponse> cartItemResponseList,
        Integer totalQuantity,
        Integer originTotalPrice,
        Integer discountTotalPrice

) {

    public static MemberCartResponse of(Cart cart, List<MemberCartItemResponse> cartItemResponseList){

        int totalQuantity = cartItemResponseList.stream().mapToInt(MemberCartItemResponse::quantity).sum();
        int originTotalPrice = cartItemResponseList.stream().mapToInt(MemberCartItemResponse::originPrice).sum();
        int discountTotalPrice = cartItemResponseList.stream().mapToInt(MemberCartItemResponse::discountPrice).sum();

        return new MemberCartResponse(
                cart.getId(),
                cartItemResponseList,
                totalQuantity,
                originTotalPrice,
                discountTotalPrice
        );
    }
}
