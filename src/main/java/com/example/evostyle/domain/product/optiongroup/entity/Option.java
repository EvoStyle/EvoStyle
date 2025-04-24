package com.example.evostyle.domain.product.optiongroup.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_group_id")
    private ProductOptionGroup productOptionGroup;

    @Column(nullable = false)
    private String type;

    private Option (ProductOptionGroup productOptionGroup, String type){
       this.productOptionGroup = productOptionGroup ;
       this.type = type;
    }

    public static Option of(ProductOptionGroup productOptionGroup, String type){
        return new Option(productOptionGroup, type);
    }
}
