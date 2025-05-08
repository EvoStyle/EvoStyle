package com.example.evostyle.domain.brand.repository;

import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.QBrandCategory;
import com.example.evostyle.domain.brand.entity.QBrandCategoryMapping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BrandCategoryQueryDslImpl implements BrandCategoryQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBrandCategory brandCategory = QBrandCategory.brandCategory;
    private final QBrandCategoryMapping brandCategoryMapping = QBrandCategoryMapping.brandCategoryMapping;

    @Override
    public List<CategoryInfo> findCategoryInfoByBrand(Brand brand) {
        return jpaQueryFactory.select(brandCategory)
                .from(brandCategoryMapping)
                .join(brandCategoryMapping.brandCategory, brandCategory)
                .where(brandCategoryMapping.brand.eq(brand))
                .fetch()
                .stream()
                .map(CategoryInfo::from)
                .toList();
    }
}
