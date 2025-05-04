package com.example.evostyle.domain.coupon.service;

import com.example.evostyle.domain.coupon.dto.request.CreateCouponRequest;
import com.example.evostyle.domain.coupon.dto.response.CreateCouponResponse;
import com.example.evostyle.domain.coupon.dto.response.IssueCouponResponse;
import com.example.evostyle.domain.coupon.entity.Coupon;
import com.example.evostyle.domain.coupon.repository.CouponRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.entity.MemberCoupon;
import com.example.evostyle.domain.member.repository.MemberCouponRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;

    public CreateCouponResponse createCoupon(CreateCouponRequest request) {
        LocalDate startAt = LocalDate.now();
        LocalDate endAt = startAt.plusDays(request.validDays());

        Coupon coupon = Coupon.of(
            request.name(), request.discountPrice(), request.maxIssueCount(),
            startAt, endAt);

        couponRepository.save(coupon);

        return CreateCouponResponse.from(coupon);
    }

    public IssueCouponResponse issueCoupon(Long couponId, Long memberId) {
        if (memberCouponRepository.existsByMemberIdAndCouponId(memberId, couponId)) {
            throw new ConflictException(ErrorCode.COUPON_ALREADY_ISSUED);
        }

        Coupon coupon = couponRepository.findByIdWithLock(couponId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

        if (coupon.getIssueCount() >= coupon.getMaxIssueCount()) {
            throw new BadRequestException(ErrorCode.COUPON_ISSUE_LIMIT_EXCEEDED);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        MemberCoupon memberCoupon = MemberCoupon.of(member, coupon);
        memberCouponRepository.save(memberCoupon);

        coupon.increaseIssueCount();

        return IssueCouponResponse.from(memberCoupon);
    }
}
