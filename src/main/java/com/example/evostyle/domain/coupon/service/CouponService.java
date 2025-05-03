package com.example.evostyle.domain.coupon.service;

import com.example.evostyle.domain.coupon.dto.request.CreateCouponRequest;
import com.example.evostyle.domain.coupon.dto.response.CreateCouponResponse;
import com.example.evostyle.domain.coupon.entity.Coupon;
import com.example.evostyle.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CreateCouponResponse createCoupon(CreateCouponRequest request) {
        Coupon coupon = Coupon.of(
            request.name(), request.discountPrice(), request.maxIssueCount(),
            request.startAt(), request.endAt());

        couponRepository.save(coupon);

        return CreateCouponResponse.from(coupon);
    }
}
