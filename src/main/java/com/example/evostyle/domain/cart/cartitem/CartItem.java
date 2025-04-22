package com.example.evostyle.domain.cart.cartitem;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.cart.entity.Cart;
import com.example.evostyle.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    private CartItem(Cart cart, Product product,  Integer quantity){
        this.cart = cart;
        this.product = product;
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    public CartItem of(Cart cart, Product product,  Integer quantity){
        return new CartItem(cart, product, quantity);
    }
}
