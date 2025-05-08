package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.BrandCategory;

public record UpdateBrandCategoryResponse(Long id, String name) {
    public static UpdateBrandCategoryResponse from(BrandCategory brandCategory) {
        return new UpdateBrandCategoryResponse(
                brandCategory.getId(),
                brandCategory.getName()
        );
    }
}
