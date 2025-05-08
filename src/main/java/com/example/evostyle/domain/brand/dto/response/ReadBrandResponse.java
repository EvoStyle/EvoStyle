package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record ReadBrandResponse(Long id, String name, List<BrandCategoryInfo> brandBrandCategoryInfoList) {

    public static ReadBrandResponse from(Brand brand, List<BrandCategoryInfo> brandBrandCategoryInfoList) {
        return new ReadBrandResponse(
                brand.getId(),
                brand.getName(),
                brandBrandCategoryInfoList
        );
    }
}
