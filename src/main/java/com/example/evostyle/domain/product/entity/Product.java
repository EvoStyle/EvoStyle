package com.example.evostyle.domain.product.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.brand.entity.Brand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @ColumnDefault("0")
    private Integer likeCount = 0;
    @Column(nullable = false)

    @ColumnDefault("0.0")
    private Float averageRating = 0.0F;

    private Product(Brand brand, Long categoryId, String name, Integer price, String description){
        this.brand = brand;
        this.categoryId = categoryId ;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static Product of(Brand brand, Long categoryId, String name, Integer price, String description){
        return new Product(brand, categoryId, name, price, description);
    }

    public void update(String name, String description, Integer price){
        if(name != null && !name.isBlank()){this.name = name;}
        if(description != null && !description.isBlank()){this.description = description;}
        if(price != null){this.price = price ;}
    }
}
