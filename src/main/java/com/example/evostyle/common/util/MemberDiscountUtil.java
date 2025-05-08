package com.example.evostyle.common.util;


import com.example.evostyle.domain.member.entity.MemberGrade;

public class MemberDiscountUtil {
    public static int calculateDiscountedPrice(MemberGrade memberGradle, int originalPrice) {
        return (int) ((long) originalPrice * (100 - memberGradle.getDiscountRate()) / 100);
    }
}
