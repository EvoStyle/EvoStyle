package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record UpdateOwnerBrandCategoryResponse(
        Long id,
        String name,
        List<CategoryInfo> updatedBrandCategoryInfoList
) {
    public static UpdateOwnerBrandCategoryResponse from(
            Brand brand,
            List<CategoryInfo> updatedBrandCategoryInfoList
    ) {
        return new UpdateOwnerBrandCategoryResponse(
                brand.getId(),
                brand.getName(),
                updatedBrandCategoryInfoList
        );
    }
}