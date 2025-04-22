package com.example.evostyle.domain.brand.brandcategory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brand_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private BrandCategory (String name) {
        this.name = name;
    }

    public static BrandCategory of(String name) {
        return new BrandCategory(name);
    }
}
