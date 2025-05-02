package com.example.evostyle.domain.member.entity;

public enum MemberGradle {

    MEMBER(0,0), FRIEND(500_000, 5), SIlVER(1_000_000,10),
    GOULD(5_000_000,15), PLATINUM(10_000_000, 20);

    private final int purchaseSum;
    private final int discountRate;

    MemberGradle(int purchaseSum, int discountRate){
        this.purchaseSum = purchaseSum;
        this.discountRate = discountRate;
    }
}