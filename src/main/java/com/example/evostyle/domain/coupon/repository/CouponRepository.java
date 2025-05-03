package com.example.evostyle.domain.coupon.repository;

import com.example.evostyle.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
