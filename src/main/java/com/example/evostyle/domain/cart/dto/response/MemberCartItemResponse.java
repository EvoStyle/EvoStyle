package com.example.evostyle.domain.cart.dto.response;

import com.example.evostyle.common.util.MemberDiscountUtil;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.cart.entity.CartItem;
import com.example.evostyle.domain.member.entity.MemberGradle;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;

public record MemberCartItemResponse(
        Long id,
        Long cartId,
        ProductResponse productResponse,
        ProductDetailResponse productDetailResponse,
        Integer quantity,
        Integer originPrice,
        Integer discountPrice
) {

    public static MemberCartItemResponse of(ProductResponse productResponse,
                                            ProductDetailResponse productDetailResponse,
                                            CartItem cartItem,
                                            MemberGradle memberGradle) {

        int originPrice = cartItem.getQuantity() * cartItem.getProductDetail().getProduct().getPrice();

        return new MemberCartItemResponse(
                cartItem.getId(),
                cartItem.getCart().getId(),
                productResponse,
                productDetailResponse,
                cartItem.getQuantity(),
                originPrice,
                MemberDiscountUtil.discountPrice(memberGradle, originPrice));

    }
}
