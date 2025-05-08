package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.ProductDetailOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDetailOptionRepository extends JpaRepository<ProductDetailOption, Long> {

    List<ProductDetailOption> findByProductDetailId(Long productDetailId);

    List<ProductDetailOption> findByProductDetailIdIn(List<Long> productDetailIdList);
}
