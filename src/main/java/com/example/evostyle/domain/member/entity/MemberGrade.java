package com.example.evostyle.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberGrade {

    MEMBER(0,0), FRIEND(500_000, 5), SIlVER(1_000_000,10),
    GOULD(5_000_000,15), PLATINUM(10_000_000, 20);

     final int purchaseSum;
     final int discountRate;

    MemberGrade(int purchaseSum, int discountRate){
        this.purchaseSum = purchaseSum;
        this.discountRate = discountRate;
    }

    public MemberGrade nextGrade(){

        MemberGrade[] values = MemberGrade.values();
        int nextOrdinal = this.ordinal() + 1;

        return nextOrdinal < values.length ? values[nextOrdinal] : this;
    }
}