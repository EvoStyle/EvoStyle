package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.dto.response.ProductCategoryInfo;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.entity.ProductCategoryMapping;
import com.example.evostyle.domain.product.entity.QProductCategory;
import com.example.evostyle.domain.product.entity.QProductCategoryMapping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCategoryQueryDslImpl implements ProductCategoryQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private final QProductCategory productCategory = QProductCategory.productCategory;
    private final QProductCategoryMapping productCategoryMapping = QProductCategoryMapping.productCategoryMapping;

    private final ProductCategoryMappingRepository productCategoryMappingRepository;

    @Override
    public List<ProductCategoryInfo> findCategoryInfoByProduct(Product product) {
        return jpaQueryFactory.select(productCategory)
                .from(productCategoryMapping)
                .join(productCategoryMapping.productCategory, productCategory)
                .where(productCategoryMapping.product.eq(product))
                .fetch()
                .stream()
                .map(ProductCategoryInfo::from)
                .toList();
    }

    @Override
    public void saveBrandCategoryMappings(List<ProductCategoryMapping> productCategoryMappingList) {
        productCategoryMappingRepository.saveAll(productCategoryMappingList);
    }
}
