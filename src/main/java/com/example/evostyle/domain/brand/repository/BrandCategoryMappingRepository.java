package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BrandCategoryMappingRepository extends JpaRepository<BrandCategoryMapping, Long> {

    void deleteByBrandId(Long brandId);

    Set<BrandCategoryMapping> findAllByBrandId(Long brandId);
}