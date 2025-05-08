package com.example.evostyle.domain.order.entity;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "is_cancelled", nullable = false)
    private boolean isCancelled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    private OrderItem(
            Integer eachAmount,
            Integer totalPrice,
            Order order,
            Brand brand,
            OrderStatus orderStatus,
            ProductDetail productDetail,
            String productName,
            Integer productPrice,
            String productDescription
    ) {
        this.eachAmount = eachAmount;
        this.totalPrice = totalPrice;
        this.order = order;
        this.brand = brand;
        this.orderStatus = orderStatus;
        this.productDetail = productDetail;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    public static OrderItem of(
            Integer eachAmount,
            Order order,
            ProductDetail productDetail
    ) {
        return new OrderItem(
                eachAmount,
                productDetail.getProduct().getPrice() * eachAmount,
                order,
                productDetail.getProduct().getBrand(),
                OrderStatus.PENDING,
                productDetail,
                productDetail.getProduct().getName(),
                productDetail.getProduct().getPrice(),
                productDetail.getProduct().getDescription()
        );
    }

    public void update(int newAmount) {
        this.eachAmount = newAmount;
        this.totalPrice = this.productPrice * newAmount;
    }

    public void markAsCancelled() {
        this.isCancelled = true;
        this.cancelledAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void validateOrderIdMatch(Long orderId) {
        boolean isDifferent = !this.order.getId().equals(orderId);

        if (isDifferent) {
            throw new NotFoundException(ErrorCode.ORDER_NOT_FOUND);
        }
    }

    public void validateProductDetailIdMatch(Long productDetailId) {
        boolean isDifferent = !this.productDetail.getId().equals(productDetailId);

        if (isDifferent) {
            throw new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }
    }
}
