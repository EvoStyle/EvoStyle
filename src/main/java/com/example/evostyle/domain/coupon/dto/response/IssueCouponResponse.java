package com.example.evostyle.domain.coupon.dto.response;

import com.example.evostyle.domain.member.entity.MemberCoupon;

public record IssueCouponResponse(
    Long couponId,
    Long memberId,
    String couponName
) {
    public static IssueCouponResponse from(MemberCoupon memberCoupon) {
        return new IssueCouponResponse(
            memberCoupon.getCoupon().getId(),
            memberCoupon.getMember().getId(),
            memberCoupon.getCoupon().getName());
    }
}
