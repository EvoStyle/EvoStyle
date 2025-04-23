package com.example.evostyle.domain.product.productdetail.entity.product_detail_option.entity;


import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import com.example.evostyle.domain.product.optiongroup.option.entity.Option;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
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
    private Long id ;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "product_option_group_id")
    private OptionGroup optionGroup;

    @ManyToOne
    @JoinColumn(name = "product_option_id")
    private Option option ;

    private ProductDetailOption(ProductDetail productDetail, OptionGroup optionGroup, Option option){
        this.productDetail = productDetail ;
        this.optionGroup = optionGroup ;
        this.option = option ;
    }

    public static ProductDetailOption of(ProductDetail productDetail, OptionGroup optionGroup, Option option){
        return new ProductDetailOption(productDetail, optionGroup, option);
    }
}
