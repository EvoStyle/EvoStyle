package com.example.evostyle.domain.order.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "total_amount_sum", nullable = false)
    private int totalAmountSum;

    @Column(name = "total_price_sum", nullable = false)
    private int totalPriceSum;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "is_cancelled", nullable = false)
    private boolean isCancelled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Order(
            Member member,
            int totalAmountSum,
            int totalPriceSum
    ) {
        this.member = member;
        this.totalAmountSum = totalAmountSum;
        this.totalPriceSum = totalPriceSum;
    }

    public static Order of(
            Member member,
            int totalAmountSum,
            int totalPriceSum
    ) {
        return new Order(
                member,
                totalAmountSum,
                totalPriceSum
        );
    }

    public void updateAmountAndPrice(
            int totalAmountSum,
            int totalPriceSum
    ) {
        this.totalAmountSum = totalAmountSum;
        this.totalPriceSum = totalPriceSum;
    }

    public void markAsCancelled() {
        this.isCancelled = true;
        this.cancelledAt = LocalDateTime.now();
    }
}