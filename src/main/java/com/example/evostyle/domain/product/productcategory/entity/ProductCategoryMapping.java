package com.example.evostyle.domain.product.productcategory.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_category_mappings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    private ProductCategoryMapping(Product product, ProductCategory productCategory) {
        this.product = product;
        this.productCategory = productCategory;
    }

    public static ProductCategoryMapping of(Product product, ProductCategory productCategory) {
        return new ProductCategoryMapping(product, productCategory);
    }

}
