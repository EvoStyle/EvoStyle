package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.BrandCategory;
import lombok.Getter;

@Getter
public class BrandCategoryInfo {

    private final Long id;
    private final String name;

    private BrandCategoryInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BrandCategoryInfo from(BrandCategory brandCategory) {
        return new BrandCategoryInfo(
                brandCategory.getId(),
                brandCategory.getName()
        );
    }
}