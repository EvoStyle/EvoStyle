package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.dto.request.BrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.BrandCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandCategoryRepository extends JpaRepository<BrandCategory, Long> {


    @Query("SELECT new com.example.evostyle.domain.brand.dto.response.CategoryInfo(bc.id, bc.name) " +
            "FROM BrandCategoryMapping bcm " +
            "JOIN bcm.brandCategory bc " +
            "WHERE bcm.brand = :brand")
    List<CategoryInfo> findCategoryInfoByBrand(Brand brand);

    List<BrandCategory> findByNameIn(List<String> brandCategoryRequest);
}
