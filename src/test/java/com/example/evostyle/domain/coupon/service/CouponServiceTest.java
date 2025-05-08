package com.example.evostyle.domain.coupon.service;

import com.example.evostyle.domain.coupon.dto.response.IssueCouponResponse;
import com.example.evostyle.domain.coupon.entity.Coupon;
import com.example.evostyle.domain.coupon.repository.CouponRepository;
import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.GenderType;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.entity.MemberCoupon;
import com.example.evostyle.domain.member.repository.MemberCouponRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 발급 서비스 테스트")
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CouponRepository couponRepository;

    @Test
    void issueCoupon_success() {
        // given
        Long couponId = 1L;
        Long memberId = 1L;

        Member member = Member.of(
            "test@gmail.com",
            "1234",
            "테스트용",
            27,
            "01025948334",
            Authority.OWNER,
            GenderType.M
        );
        ReflectionTestUtils.setField(member, "id", memberId);

        Coupon coupon = Coupon.of(
            "테스트 쿠폰",
            5000,
            10,
            LocalDate.of(2025, 5, 3),
            LocalDate.of(2025, 5, 10)
        );
        ReflectionTestUtils.setField(coupon, "id", couponId);

        when(memberCouponRepository.existsByMemberIdAndCouponId(memberId, couponId)).thenReturn(false);
        when(couponRepository.findByIdWithLock(couponId)).thenReturn(Optional.of(coupon));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        MemberCoupon memberCoupon = MemberCoupon.of(member, coupon);

        ReflectionTestUtils.setField(memberCoupon, "id", 1L);

        when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

        // when
        IssueCouponResponse response = couponService.issueCoupon(couponId, memberId);

        // then
        assertThat(response.couponId()).isEqualTo(couponId);
        assertThat(response.memberId()).isEqualTo(memberId);
        assertThat(response.couponName()).isEqualTo("테스트 쿠폰");

        verify(memberCouponRepository).save(any(MemberCoupon.class));
        verify(couponRepository).findByIdWithLock(couponId);
        verify(memberRepository).findById(memberId);
    }
}
