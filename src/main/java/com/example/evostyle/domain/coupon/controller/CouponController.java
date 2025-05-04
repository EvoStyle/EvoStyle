package com.example.evostyle.domain.coupon.controller;

import com.example.evostyle.domain.coupon.dto.request.CreateCouponRequest;
import com.example.evostyle.domain.coupon.dto.response.CreateCouponResponse;
import com.example.evostyle.domain.coupon.dto.response.IssueCouponResponse;
import com.example.evostyle.domain.coupon.service.CouponService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CreateCouponResponse> createCoupon(
        @RequestBody CreateCouponRequest request
    ) {
        CreateCouponResponse createCouponResponse = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createCouponResponse);
    }

    @PostMapping("coupons/{couponId}/issue")
    public ResponseEntity<IssueCouponResponse> issueCoupon(
        @PathVariable(name = "couponId") Long couponId,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        IssueCouponResponse issueCouponResponse = couponService.issueCoupon(couponId, memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(issueCouponResponse);
    }
}
