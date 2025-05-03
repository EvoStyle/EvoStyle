package com.example.evostyle.domain.coupon.controller;

import com.example.evostyle.domain.coupon.dto.request.CreateCouponRequest;
import com.example.evostyle.domain.coupon.dto.response.CreateCouponResponse;
import com.example.evostyle.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
