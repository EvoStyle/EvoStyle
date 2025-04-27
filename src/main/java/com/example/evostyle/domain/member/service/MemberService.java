package com.example.evostyle.domain.member.service;

import com.example.evostyle.domain.member.dto.request.UpdateMemberRequest;
import com.example.evostyle.domain.member.dto.response.MemberResponse;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.ForbiddenException;
import com.example.evostyle.global.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse readMember(HttpServletRequest request) {
        Long loginMemberId = (Long) request.getAttribute("memberId");

        Member member = memberRepository.findByIdAndIsDeletedFalse(loginMemberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateMember(UpdateMemberRequest request, HttpServletRequest httpServletRequest) {
        Long loginMemberId = (Long) httpServletRequest.getAttribute("memberId");

        Member member = memberRepository.findByIdAndIsDeletedFalse(loginMemberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateMember(request.nickname(), request.age(), request.phoneNumber());

        return MemberResponse.from(member);
    }

    @Transactional
    public void deleteMember(Long memberId, HttpServletRequest request) {
        Long loginMemberId = (Long) request.getAttribute("memberId");

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getId().equals(loginMemberId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        member.deleteMember();
    }
}
