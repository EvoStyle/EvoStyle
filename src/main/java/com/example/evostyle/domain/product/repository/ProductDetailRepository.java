package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
}
