package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.common.util.MemberDiscountUtil;
import com.example.evostyle.domain.cart.entity.CartItem;
import com.example.evostyle.domain.member.entity.MemberGrade;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;

public record MemberCartItemResponse(
        Long id,
        Long cartId,
        ProductDetailResponse productDetailResponse,
        Integer quantity,
        Integer originPrice,
        Integer discountPrice
) {

    public static MemberCartItemResponse of(ProductDetailResponse productDetailResponse,
                                            CartItem cartItem,
                                            MemberGrade memberGrade) {

        int originPrice = cartItem.getQuantity() * cartItem.getProductDetail().getProduct().getPrice();

        return new MemberCartItemResponse(
                cartItem.getId(),
                cartItem.getCart().getId(),
                productDetailResponse,
                cartItem.getQuantity(),
                originPrice,
                MemberDiscountUtil.calculateDiscountedPrice(memberGrade, originPrice));
    }
}
