package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.ProductCategory;

public record UpdateProductCategoryResponse(Long id, String name) {
    public static UpdateProductCategoryResponse from(ProductCategory productCategory) {
        return new UpdateProductCategoryResponse(
                productCategory.getId(),
                productCategory.getName()
        );
    }
}
