package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.entity.Brand;

import java.util.List;

public interface BrandCategoryQueryDsl {

    List<CategoryInfo> findCategoryInfoByBrand(Brand brand);
}
