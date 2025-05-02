package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record UpdateBrandCategoryResponse(
        Long id,
        String name,
        List<CategoryInfo> updatedBrandCategoryInfoList
) {
    public static UpdateBrandCategoryResponse from(
            Brand brand,
            List<CategoryInfo> updatedBrandCategoryInfoList
    ) {
        return new UpdateBrandCategoryResponse(
                brand.getId(),
                brand.getName(),
                updatedBrandCategoryInfoList
        );
    }
}