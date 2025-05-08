package com.example.evostyle.domain.product.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("false")
    private boolean isDeleted = false;

    private ProductDetailOption(ProductDetail productDetail, Option option) {
        this.productDetail = productDetail;
        this.option = option;
    }

    public static ProductDetailOption of(ProductDetail productDetail, Option option) {
        return new ProductDetailOption(productDetail, option);
    }

    public void delete(){
        this.isDeleted = false;
    }
}
