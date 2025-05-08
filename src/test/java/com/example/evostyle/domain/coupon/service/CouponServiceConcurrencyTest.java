package com.example.evostyle.domain.coupon.service;

import com.example.evostyle.domain.coupon.entity.Coupon;
import com.example.evostyle.domain.coupon.repository.CouponRepository;
import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.GenderType;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberCouponRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class CouponServiceConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    private Long couponId;
    private final int MAX_ISSUE_COUNT = 10;
    private final int THREAD_COUNT = 32;

    @BeforeEach
    void setUp() {
        Coupon coupon = Coupon.of(
            "테스트 쿠폰", 5000, MAX_ISSUE_COUNT,
            LocalDate.now(), LocalDate.now().plusDays(7));

        couponRepository.save(coupon);
        couponId = coupon.getId();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Member member = Member.of(
                "user" + i + "@email.com", "1234", "테스트 사용자 " + i, 27,
                String.format("010%08d", i), Authority.CUSTOMER, GenderType.M);
            memberRepository.save(member);
        }
    }

    @Test
    void 동시에_100명이_쿠폰을_요청하면_중복없이_10명만_쿠폰발급() throws InterruptedException{
        // given
        ExecutorService excutor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        List<Member> memberList = memberRepository.findAll();

        long start = System.currentTimeMillis();

        // when
        for (Member member : memberList) {
            excutor.submit(() -> {
                try {
                    couponService.issueCoupon(couponId, member.getId());
                } catch (Exception ignored) {

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long end = System.currentTimeMillis();

        // then
        long count = memberCouponRepository.count();
        assertEquals(MAX_ISSUE_COUNT, count);

        log.info("✅ Thread: {}명 | ⏱ Duration: {}ms | 🎟 Issued: {}개", THREAD_COUNT, end - start, count);
    }
}
