package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record UpdateBrandNameResponse(
        Long id,
        String updatedName,
        List<BrandCategoryInfo> brandBrandCategoryInfoList
) {
    public static UpdateBrandNameResponse from(
            Brand brand,
            List<BrandCategoryInfo> brandBrandCategoryInfoList
    ) {
        return new UpdateBrandNameResponse(
                brand.getId(),
                brand.getName(),
                brandBrandCategoryInfoList
        );
    }
}