package com.example.evostyle.domain.order.entity;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "product_name", nullable = false, length = 10)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Integer productPrice;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    private OrderItem(
            Integer eachAmount,
            Integer totalPrice,
            Order order,
            OrderStatus orderStatus,
            ProductDetail productDetail,
            String productName,
            Integer productPrice,
            String productDescription
    ) {
        this.eachAmount = eachAmount;
        this.totalPrice = totalPrice;
        this.order = order;
        this.orderStatus = orderStatus;
        this.productDetail = productDetail;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    public static OrderItem of(
            Integer eachAmount,
            Integer totalPrice,
            Order order,
            OrderStatus orderStatus,
            ProductDetail productDetail,
            String productName,
            Integer productPrice,
            String productDescription
    ) {
        return new OrderItem(
                eachAmount,
                totalPrice,
                order,
                orderStatus,
                productDetail,
                productName,
                productPrice,
                productDescription
        );
    }

    public void update(Integer eachAmount) {
        this.eachAmount = eachAmount;
    };
}
