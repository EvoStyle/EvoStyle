package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.productdetail.entity.ProductDetailOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailOptionRepository extends JpaRepository<ProductDetailOption, Long> {
}
