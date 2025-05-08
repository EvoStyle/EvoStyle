package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.dto.response.BrandCategoryInfo;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;

import java.util.List;

public interface BrandCategoryQueryDsl {

    List<BrandCategoryInfo> findCategoryInfoByBrand(Brand brand);

    void deleteAllByBrandId(Long brandId);

    void saveBrandCategoryMappings(List<BrandCategoryMapping> brandCategoryMappingList);
}
