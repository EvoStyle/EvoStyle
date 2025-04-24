package com.example.evostyle.domain.product.optiongroup.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_option_groups")
@NoArgsConstructor
public class ProductOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionGroup optionGroup;

    private ProductOptionGroup(OptionGroup optionGroup){
        this.optionGroup = optionGroup ;
    }

    public ProductOptionGroup of(OptionGroup optionGroup){
        return new ProductOptionGroup(optionGroup);
    }
}
