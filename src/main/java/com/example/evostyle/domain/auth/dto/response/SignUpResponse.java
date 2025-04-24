package com.example.evostyle.domain.auth.dto.response;

import com.example.evostyle.domain.member.entity.Member;

public record SignUpResponse(Long id, String email, String nickname) {

    public static SignUpResponse from(Member member) {
        return new SignUpResponse(member.getId(), member.getEmail(), member.getNickname());
    }
}
