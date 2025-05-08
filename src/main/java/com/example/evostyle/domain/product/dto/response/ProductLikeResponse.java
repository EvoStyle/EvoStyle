package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.ProductLike;

public record ProductLikeResponse(
        Long id,
        Long memberId,
        Long productId
) {

    public static ProductLikeResponse from(ProductLike productLike){
        return new ProductLikeResponse(
                productLike.getId(),
                productLike.getMember().getId(),
                productLike.getProduct().getId()
        );
    }
}
