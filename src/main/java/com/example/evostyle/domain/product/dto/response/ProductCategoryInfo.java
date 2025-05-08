package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.ProductCategory;
import lombok.Getter;

@Getter
public class ProductCategoryInfo {
    private final Long id;
    private final String name;

    private ProductCategoryInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ProductCategoryInfo from(ProductCategory productCategory) {
        return new ProductCategoryInfo(
                productCategory.getId(),
                productCategory.getName()
        );
    }

}
