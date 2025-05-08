package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Member;

import java.util.List;

public interface MemberQueryRepository {
    List<Member> findDuplicates(String email, String nickname, String phoneNumber);
}
