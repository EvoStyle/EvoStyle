package com.example.evostyle.domain.cart.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.product.entity.ProductDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_detail_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Column(name = "quantity")
    private Integer quantity;

    private CartItem(Cart cart, ProductDetail productDetail, Integer quantity) {
        this.cart = cart;
        this.productDetail = productDetail;
        this.quantity = quantity;
    }

    public static CartItem of(Cart cart, ProductDetail productDetail, Integer quantity) {
        return new CartItem(cart, productDetail, quantity);
    }

    public void updateQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public void mergeQuantity(Integer quantity){this.quantity += quantity; }
}
