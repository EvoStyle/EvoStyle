package com.example.evostyle.domain.product.productdetail.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Setter
    @Column(name = "product_stock")
    @ColumnDefault("0")
    private Integer stock = 0 ;

    @ColumnDefault("false")
    private boolean isDeleted = false;

    private ProductDetail(Product product){
        this.product = product ;
    }

    public static ProductDetail of(Product product){
        return new ProductDetail(product);
    }

}
