package com.example.evostyle.domain.product.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product_details")
@NoArgsConstructor
public class ProductDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_stock")
    private Integer stock;

    private ProductDetail(Product product, Integer stock){
        this.product = product ;
        this.stock = stock ;
    }

    public ProductDetail of(Product product, Integer stock){
        return new ProductDetail(product, stock);
    }

    public void decreaseStock(int amount) {
        if(amount <= 0) {
            throw new BadRequestException(ErrorCode.INVALID_STOCK_DECREASE_AMOUNT);
        }

        if(this.stock < amount) {
            throw new BadRequestException(ErrorCode.OUT_OF_STOCK);
        }

        this.stock -= amount;
    }
}