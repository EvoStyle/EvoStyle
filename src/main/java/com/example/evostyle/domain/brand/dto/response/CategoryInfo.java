package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.brandcategory.BrandCategory;
import lombok.Getter;

@Getter
public class CategoryInfo {

    private final Long id;
    private final String name;

    public CategoryInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryInfo from(BrandCategory brandCategory) {
        return new CategoryInfo(
                brandCategory.getId(),
                brandCategory.getName()
        );
    }
}