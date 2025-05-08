package com.example.evostyle.common.util;

import com.example.evostyle.domain.member.entity.MemberGradle;


public class MemberDiscountUtil {
    public static int calculateDiscountedPrice(MemberGradle memberGradle, int originalPrice) {
        return (int) ((long) originalPrice * (100 - memberGradle.getDiscountRate()) / 100);
    }
}
