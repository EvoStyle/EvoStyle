package com.example.evostyle.domain.orderitem.entity;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "each_amount", nullable = false)
    private Integer eachAmount;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    private OrderItem(Integer eachAmount, Integer totalPrice, Order order, ProductDetail productDetail) {
        this.eachAmount = eachAmount;
        this.totalPrice = totalPrice;
        this.order = order;
        this.productDetail = productDetail;
    }

    public static OrderItem of(Integer eachAmount, Integer totalPrice, Order order, ProductDetail productDetail) {
        return new OrderItem(eachAmount, totalPrice, order, productDetail);
    }
}
