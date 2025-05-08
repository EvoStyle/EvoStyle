package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    boolean existsByMemberIdAndCouponId(Long memberId, Long couponId);
}
