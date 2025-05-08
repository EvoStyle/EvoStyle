package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);
}