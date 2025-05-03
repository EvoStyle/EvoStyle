package com.example.evostyle.domain.coupon.dto.request;

import java.time.LocalDate;

public record CreateCouponRequest(
    String name,
    Integer discountPrice,
    Integer maxIssueCount,
    LocalDate startAt,
    LocalDate endAt

) {
}
