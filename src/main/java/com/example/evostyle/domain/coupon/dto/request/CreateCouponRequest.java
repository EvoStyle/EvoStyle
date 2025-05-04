package com.example.evostyle.domain.coupon.dto.request;

public record CreateCouponRequest(
    String name,
    Integer discountPrice,
    Integer maxIssueCount,
    Integer validDays
) {
}
