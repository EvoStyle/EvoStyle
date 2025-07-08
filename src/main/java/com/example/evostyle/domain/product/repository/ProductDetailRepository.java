package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByProductId(Long productId);

    @Modifying
    @Query("""
            UPDATE ProductDetail p
            SET p.stock = p.stock - :quantity
            WHERE p.id = :productDetailId AND p.stock >= :quantity
            """)
    int decreaseStock(@Param("productDetailId") Long productDetailId, @Param("quantity") Integer quantity);


    @Modifying
    @Query("UPDATE ProductDetail p SET p.stock = p.stock + :quantity WHERE p.id = :productDetailId")
    int increaseStock(@Param("productDetailId") Long productDetailId, @Param("quantity") Integer quantity);


    @Modifying
    @Query("UPDATE ProductDetail p SET p.stock = :quantity WHERE p.id = :productDetailId")
    int updateStock(@Param("productDetailId") Long productDetailId, @Param("quantity") Integer quantity);
}
