package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public record CreateBrandResponse(Long id, String name, List<CategoryInfo> brandCategoryInfoList) {

    public static CreateBrandResponse from(Brand brand, List<CategoryInfo> brandCategoryInfoList) {
        return new CreateBrandResponse(
                brand.getId(),
                brand.getName(),
                brandCategoryInfoList
        );
    }
}