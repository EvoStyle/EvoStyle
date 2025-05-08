package com.example.evostyle.domain.product.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.time.LocalDateTime;

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

    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Product(Brand brand, String name, Integer price, String description){
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static Product of(
            Brand brand,
            String name,
            Integer price,
            String description,
            List<ProductCategory> productCategoryList
    ){
        validateProductCategoryLimit(productCategoryList);

        return new Product(brand, name, price, description);
    }

    private static void validateProductCategoryLimit(List<ProductCategory> productCategoryList) {
        if (productCategoryList.size() > ProductCategoryLimit.MAX_CATEGORY_COUNT) {
            throw new BadRequestException(ErrorCode.CATEGORY_LIMIT_EXCEEDED);
        }

        if (productCategoryList.isEmpty()) {
            throw new BadRequestException(ErrorCode.NON_EXISTENT_CATEGORY);
        }
    }

    public void update(String name, String description, Integer price){
        if(name != null && !name.isBlank()){this.name = name;}
        if(description != null && !description.isBlank()){this.description = description;}
        if(price != null){this.price = price ;}
    }

    public void delete(){
        this.isDeleted = false;
        this.deletedAt = LocalDateTime.now();
    }

}
