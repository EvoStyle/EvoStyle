package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, ProductCategoryQueryDsl {

    boolean existsByName(String name);

    List<ProductCategory> findByNameIn(List<String> nameList);
}
