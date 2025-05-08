package com.example.evostyle.domain.product.repository;


import com.example.evostyle.domain.cart.entity.CartItem;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetailOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByProductId(Long productId);


}
