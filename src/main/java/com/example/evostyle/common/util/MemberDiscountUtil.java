package com.example.evostyle.common.util;

import com.example.evostyle.domain.member.entity.MemberGradle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberDiscountUtil {
    public static int discountPrice(MemberGradle memberGradle, int originalPrice){
        return originalPrice - calculateDiscountAmount(originalPrice, memberGradle);
    }

    public static int calculateDiscountAmount(int originalPrice, MemberGradle memberGradle) {
        return originalPrice * memberGradle.getDiscountRate() / 100;
    }
}
