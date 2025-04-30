package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.id IN :idList")
    List<Product> findByIdList(@Param("idList") List<Long> idList);
}
