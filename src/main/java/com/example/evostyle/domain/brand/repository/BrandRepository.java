package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    Optional<Brand> findByIdAndIsDeletedFalse(Long brandId);
}