package com.example.evostyle.domain.member.dto.response;

import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.GenderType;
import com.example.evostyle.domain.member.entity.Member;

public record MemberResponse(
    Long id, String email, String nickname,
    int age, Authority authority, GenderType genderType
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getNickname(),
            member.getAge(),
            member.getAuthority(),
            member.getGenderType());
    }
}
