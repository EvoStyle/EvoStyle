package com.example.evostyle.domain.product.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.brand.entity.Brand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "product_details")
@NoArgsConstructor
public class ProductDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("COMING_SOON")
    private ProductDetailStatus productDetailStatus = ProductDetailStatus.COMING_SOON;

    @Column(name = "product_stock")
    @ColumnDefault("0")
    private Integer stock = 0;

    @ColumnDefault("false")
    private boolean isDeleted = false;


    private ProductDetail(Product product,Brand brand) {
        this.product = product;
        this.brand = brand;
    }

    public static ProductDetail of(Product product) {
        return new ProductDetail(product,product.getBrand());
    }

    public void delete(){this.isDeleted = false;}

    public boolean updateStock(Integer stock){
        if(this.getProductDetailStatus().equals(ProductDetailStatus.ON_SALE)){return false;}
        this.stock = stock;
        return true;
    }// 관리자용

    public boolean decreaseStock(int quantity){
        if(this.stock < quantity || this.productDetailStatus.equals(ProductDetailStatus.SOLD_OUT)){return false;}
        this.stock -= quantity;
        if(this.stock == 0){this.productDetailStatus = ProductDetailStatus.SOLD_OUT;}
        return true;
    }

    public void increaseStock(int quantity){
        this.stock += quantity;
    }
}
