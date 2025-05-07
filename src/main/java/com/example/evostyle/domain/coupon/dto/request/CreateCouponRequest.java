package com.example.evostyle.domain.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCouponRequest(
    @NotBlank String name,
    @NotNull @Positive(message = "할인 금액은 0보다 커야 합니다.") Integer discountPrice,
    @NotNull @Positive(message = "최대 쿠폰 발급 수는 0보다 커야 합니다.") Integer maxIssueCount,
    @NotNull @Positive(message = "유효 기간은 0보다 커야 합니다.") Integer validDays
) {
}
