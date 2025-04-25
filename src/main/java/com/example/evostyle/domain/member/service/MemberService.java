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
import com.example.evostyle.global.exception.UnauthorizedException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final EntityManager entityManager;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberResponse readMember(Long memberId, HttpServletRequest request) {
        enableSoftDeleteFilter();

        Long loginMemberId = LoginMemberUtil.getMemberId(request, jwtUtil);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getId().equals(loginMemberId)) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, UpdateMemberRequest request, HttpServletRequest httpServletRequest) {
        enableSoftDeleteFilter();

        Long loginMemberId = LoginMemberUtil.getMemberId(httpServletRequest, jwtUtil);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getId().equals(loginMemberId)) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        member.updateMember(request.nickname(), request.age(), request.phoneNumber());

        return MemberResponse.from(member);
    }

    @Transactional
    public DeleteMemberResponse deleteMember(Long memberId, HttpServletRequest request) {
        enableSoftDeleteFilter();

        Long loginMemberId = LoginMemberUtil.getMemberId(request, jwtUtil);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getId().equals(loginMemberId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        member.deleteMember();

        return DeleteMemberResponse.from(member);
    }

    // soft delete 적용하는 filter
    private void enableSoftDeleteFilter() {
        entityManager.unwrap(Session.class)
            .enableFilter("deletedFilter")
            .setParameter("isDeleted", false);
    }
}
