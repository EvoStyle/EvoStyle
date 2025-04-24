package com.example.evostyle.domain.product.optiongroup.entity;

import com.example.evostyle.domain.product.entity.Product;
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


    private OptionGroup (String name) {
        this.name = name ;
    }

    public static OptionGroup of(String name) {
        return new OptionGroup(name);
    }
}
