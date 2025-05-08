package com.example.evostyle.domain.cart.repository;

import com.example.evostyle.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("""
            SELECT c
            FROM CartItem c
            WHERE c.cart.id = :cartId
            """)
    List<CartItem> findByCartId(@Param("cartId") Long cartId);

    boolean existsByCartIdAndProductDetailId(Long cartId, Long productDetailId);


    @Query("""
            SELECT i
            FROM CartItem i
            WHERE i.cart.id = :cartId AND i.productDetail.id = :productDetailId
            """)
    Optional<CartItem> findByCartIdAndProductDetailId(@Param("cartId") Long cartId, @Param("productDetailId") Long productDetailId);



}
