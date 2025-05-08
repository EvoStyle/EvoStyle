package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record CreateBrandResponse(Long id, String name, List<BrandCategoryInfo> brandBrandCategoryInfoList) {

    public static CreateBrandResponse from(Brand brand, List<BrandCategoryInfo> brandBrandCategoryInfoList) {
        return new CreateBrandResponse(
                brand.getId(),
                brand.getName(),
                brandBrandCategoryInfoList
        );
    }
}