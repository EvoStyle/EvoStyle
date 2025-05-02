package com.example.evostyle.domain.product.productdetail.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.product.entity.Product;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_stock")
    private Integer stock;

    private ProductDetail(Product product, Integer stock) {
        this.product = product;
        this.stock = stock;
    }

    public ProductDetail of(Product product, Integer stock) {
        return new ProductDetail(product, stock);
    }

    public void adjustStock(int previousAmount, int newAmount) {
        int difference = newAmount - previousAmount;

        if (difference > 0) {
            // 수량이 증가하면, 재고가 충분한지 확인 후 차감
            if (this.stock < difference) {
                throw new BadRequestException(ErrorCode.OUT_OF_STOCK);
            }
            this.stock -= difference;
        } else if (difference < 0) {

            // 수량이 감소하면, 감소된 수량만큼 재고 증가
            this.stock += Math.abs(difference);
        }
    }
}