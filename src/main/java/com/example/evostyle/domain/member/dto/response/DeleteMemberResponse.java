package com.example.evostyle.domain.member.dto.response;

import com.example.evostyle.domain.member.entity.Member;

public record DeleteMemberResponse(Long id) {

    public static DeleteMemberResponse from(Member member) {
        return new DeleteMemberResponse(member.getId());
    }
}
