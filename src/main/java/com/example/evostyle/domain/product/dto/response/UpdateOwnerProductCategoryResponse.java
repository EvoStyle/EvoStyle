package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.Product;

import java.util.List;

public record UpdateOwnerProductCategoryResponse(
        Long id,
        String name,
        List<ProductCategoryInfo> updatedProductCategoryInfoList
) {
    public static UpdateOwnerProductCategoryResponse from(
            Product product,
            List<ProductCategoryInfo> updatedProductCategoryInfoList
    ) {
        return new UpdateOwnerProductCategoryResponse(
                product.getId(),
                product.getName(),
                updatedProductCategoryInfoList
        );
    }
}