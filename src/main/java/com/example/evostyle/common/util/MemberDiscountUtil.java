package com.example.evostyle.common.util;

import com.example.evostyle.domain.member.entity.MemberGradle;

public class MemberDiscountUtil {
    public static int discountPrice(MemberGradle memberGradle, int originalPrice){
        return originalPrice * (100 - memberGradle.getDiscountRate()) / 100;
    }
}
