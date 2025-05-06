package com.example.evostyle.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private ProductCategory(String name){
        this.name = name ;
    }

    public static ProductCategory of(String name){
        return new ProductCategory(name);
    }

    public void update(String name) {
        this.name = name;
    }
}
