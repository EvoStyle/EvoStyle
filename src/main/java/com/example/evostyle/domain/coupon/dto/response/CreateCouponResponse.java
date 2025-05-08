package com.example.evostyle.domain.coupon.dto.response;

import com.example.evostyle.domain.coupon.entity.Coupon;

public record CreateCouponResponse(
    Long id,
    String name,
    Integer discountPrice,
    Integer maxIssueCount
) {
    public static CreateCouponResponse from(Coupon coupon) {
        return new CreateCouponResponse(
            coupon.getId(),
            coupon.getName(),
            coupon.getDiscountPrice(),
            coupon.getMaxIssueCount());
    }
}
