package com.example.evostyle.domain.product.optiongroup.entity;

import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "option_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    private OptionGroup (String name, ProductDetail productDetail) {
        this.name = name;
        this.productDetail = productDetail;
    }

    public static OptionGroup of(String name, ProductDetail productDetail) {
        return new OptionGroup(name, productDetail);
    }
}
