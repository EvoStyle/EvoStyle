package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.ProductCategory;

public record ReadProductCategoryResponse(Long id, String name) {
    public static ReadProductCategoryResponse from(ProductCategory productCategory) {
        return new ReadProductCategoryResponse(
                productCategory.getId(),
                productCategory.getName()
        );
    }
}
