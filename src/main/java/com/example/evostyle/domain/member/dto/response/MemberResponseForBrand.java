package com.example.evostyle.domain.member.dto.response;

import com.example.evostyle.domain.member.entity.Member;

public record MemberResponseForBrand(
        String nickname,
        String phoneNumber
) {
    public static MemberResponseForBrand from(Member member) {
        return new MemberResponseForBrand(member.getNickname(), member.getPhoneNumber());
    }
}
