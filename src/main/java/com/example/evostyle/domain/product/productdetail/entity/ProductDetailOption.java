package com.example.evostyle.domain.product.productdetail.entity;


import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_detail_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;


    @ManyToOne
    @JoinColumn(name = "product_option_id")
    private Option option;

    private ProductDetailOption(ProductDetail productDetail, Option option) {
        this.productDetail = productDetail;
        this.option = option;
    }

    public static ProductDetailOption of(ProductDetail productDetail, Option option) {
        return new ProductDetailOption(productDetail, option);
    }
}
