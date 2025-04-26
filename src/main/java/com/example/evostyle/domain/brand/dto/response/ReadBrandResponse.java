package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record ReadBrandResponse(Long id, String name, List<CategoryInfo> brandCategoryInfoList) {

    public static ReadBrandResponse from(Brand brand, List<CategoryInfo> brandCategoryInfoList) {
        return new ReadBrandResponse(
                brand.getId(),
                brand.getName(),
                brandCategoryInfoList
        );
    }
}
