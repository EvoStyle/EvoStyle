package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandCategoryMappingRepository extends JpaRepository<BrandCategoryMapping, Long> {

    void deleteByBrandId(Long brandId);

    boolean existsByBrandIdAndBrandCategoryId(Long brandId, Long brandCategoryId);

    Optional<BrandCategoryMapping> findByBrandIdAndBrandCategoryId(Long  brandId, Long brandCategoryId);

    List<BrandCategoryMapping> findAllByBrandId(Long brandId);
}