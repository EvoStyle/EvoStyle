package com.example.evostyle.domain.product.productdetail.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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

    @ColumnDefault("false")
    private boolean isDeleted = false;

    private LocalDateTime deleted_at;

    private ProductDetail(Product product, Integer stock){
        this.product = product ;
        this.stock = stock ;
    }

    public static ProductDetail of(Product product, Integer stock){
        return new ProductDetail(product, stock);
    }

    public void delete(){
        this.isDeleted = true;
        this.deleted_at = LocalDateTime.now();
    }
}
