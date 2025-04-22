package com.example.evostyle.domain.order.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_amount_sum", nullable = false)
    private int totalAmountSum;

    @Column(name = "total_price_sum", nullable = false)
    private int totalPriceSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    private Order(Member member, Brand brand, OrderStatus orderStatus, int totalAmountSum, int totalPriceSum) {
        this.member = member;
        this.brand = brand;
        this.orderStatus = orderStatus;
        this.totalAmountSum = totalAmountSum;
        this.totalPriceSum = totalPriceSum;
    }

    public static Order of(Member member, Brand brand, OrderStatus orderStatus, int totalAmountSum, int totalPriceSum) {
        return new Order(member, brand, orderStatus, totalAmountSum, totalPriceSum);
    }
}
