package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.BrandCategory;

public record ReadProductCategoryResponse(Long id, String name) {
    public static ReadProductCategoryResponse from(BrandCategory brandCategory) {
        return new ReadProductCategoryResponse(
                brandCategory.getId(),
                brandCategory.getName()
        );
    }
}
