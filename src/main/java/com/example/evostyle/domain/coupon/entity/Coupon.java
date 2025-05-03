package com.example.evostyle.domain.coupon.entity;

import com.example.evostyle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;

    @Column(name = "max_issue_count", nullable = false)
    private Integer maxIssueCount;

    @Column(name = "start_at", nullable = false)
    private LocalDate startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDate endAt;

    private Coupon(
            String name, Integer discountPrice, Integer maxIssueCount,
            LocalDate startAt, LocalDate endAt
    ) {
        this.name = name;
        this.discountPrice = discountPrice;
        this.maxIssueCount = maxIssueCount;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Coupon of(
        String name, Integer discountPrice, Integer maxIssueCount,
        LocalDate startAt, LocalDate endAt
    ) {
        return new Coupon(name, discountPrice, maxIssueCount, startAt, endAt);
    }
}
