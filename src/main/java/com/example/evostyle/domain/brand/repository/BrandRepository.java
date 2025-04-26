package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    List<Brand> findByIsDeletedFalse();

    Optional<Brand> findByIdAndIsDeletedFalse(Long id);

}