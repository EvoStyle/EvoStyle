package com.example.evostyle.domain.member.service;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.common.util.LoginMemberUtil;
import com.example.evostyle.domain.member.dto.request.UpdateMemberRequest;
import com.example.evostyle.domain.member.dto.response.DeleteMemberResponse;
import com.example.evostyle.domain.member.dto.response.MemberResponse;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.ForbiddenException;
import com.example.evostyle.global.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }

    public MemberResponse updateMember(Long memberId, UpdateMemberRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateMember(request.nickname(), request.age(), request.phoneNumber());

        return MemberResponse.from(member);
    }

    public DeleteMemberResponse deleteMember(Long memberId, HttpServletRequest request) {
        Long loginMemberId = LoginMemberUtil.getMemberId(request, jwtUtil);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getId().equals(loginMemberId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        memberRepository.delete(member);

        return DeleteMemberResponse.from(member);
    }
}
