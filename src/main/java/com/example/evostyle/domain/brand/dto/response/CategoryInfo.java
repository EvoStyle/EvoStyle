package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.brandcategory.BrandCategory;

public record CategoryInfo(Long id, String name) {

    public static CategoryInfo from(BrandCategory brandCategory) {
        return new CategoryInfo(
                brandCategory.getId(),
                brandCategory.getName()
        );
    }
}