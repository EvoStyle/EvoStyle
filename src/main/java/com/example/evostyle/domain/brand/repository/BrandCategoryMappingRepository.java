package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandCategoryMappingRepository extends JpaRepository<BrandCategoryMapping, Long> {
}