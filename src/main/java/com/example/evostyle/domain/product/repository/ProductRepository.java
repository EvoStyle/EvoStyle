package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBrandId(Long brandId);


    @Modifying
    @Query("UPDATE Product p SET p.likeCount = :count WHERE p.id = :productId")
    void updateLikeCount(@Param("productId") Long productId, @Param("count") int count);
}
