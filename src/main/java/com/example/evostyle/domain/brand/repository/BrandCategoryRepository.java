package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.BrandCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandCategoryRepository extends JpaRepository<BrandCategory, Long> {

    boolean existsByName(String name);

    List<BrandCategory> findByNameIn(List<String> nameList);
}
