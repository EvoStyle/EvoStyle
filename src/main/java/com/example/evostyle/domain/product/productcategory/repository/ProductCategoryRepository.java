package com.example.evostyle.domain.product.productcategory.repository;

import com.example.evostyle.domain.product.productcategory.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
