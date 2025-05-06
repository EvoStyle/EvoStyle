package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.ProductCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryMappingRepository extends JpaRepository<ProductCategoryMapping, Long> {
}
