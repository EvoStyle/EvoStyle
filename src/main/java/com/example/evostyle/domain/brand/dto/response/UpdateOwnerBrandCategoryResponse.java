package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record UpdateOwnerBrandCategoryResponse(
        Long id,
        String name,
        List<BrandCategoryInfo> updatedBrandBrandCategoryInfoList
) {
    public static UpdateOwnerBrandCategoryResponse from(
            Brand brand,
            List<BrandCategoryInfo> updatedBrandBrandCategoryInfoList
    ) {
        return new UpdateOwnerBrandCategoryResponse(
                brand.getId(),
                brand.getName(),
                updatedBrandBrandCategoryInfoList
        );
    }
}