package com.example.evostyle.domain.brand.brandcategory;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.brand.entity.Brand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "brand_category_mappings",
        uniqueConstraints = {@UniqueConstraint(
                name = "uk_brand_category_combination",
                columnNames = "brand_id, brand_category_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandCategoryMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_category_id", nullable = false)
    private BrandCategory brandCategory;

    private BrandCategoryMapping(Brand brand, BrandCategory brandCategory) {
        this.brand = brand;
        this.brandCategory = brandCategory;
    }

    public static BrandCategoryMapping of(Brand brand, BrandCategory brandCategory) {
        return new BrandCategoryMapping(brand, brandCategory);
    }
}
