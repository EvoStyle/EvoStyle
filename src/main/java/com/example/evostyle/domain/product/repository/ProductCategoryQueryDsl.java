package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.dto.response.ProductCategoryInfo;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.entity.ProductCategoryMapping;

import java.util.List;

public interface ProductCategoryQueryDsl {

    List<ProductCategoryInfo> findCategoryInfoByProduct(Product product);

    void saveBrandCategoryMappings(List<ProductCategoryMapping> brandCategoryMappingList);
}
