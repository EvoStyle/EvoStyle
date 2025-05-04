package com.example.evostyle.domain.member.controller;

import com.example.evostyle.domain.member.dto.request.UpdateMemberRequest;
import com.example.evostyle.domain.member.dto.response.MemberResponse;
import com.example.evostyle.domain.member.service.MemberService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> readMember(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        MemberResponse memberResponse = memberService.readMember(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @PatchMapping
    public ResponseEntity<MemberResponse> updateMember(
        @RequestBody UpdateMemberRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        MemberResponse memberResponse = memberService.updateMember(request, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, Long>> deleteMember(
        @PathVariable(name = "memberId") Long memberId,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long loginMemberId = authUser.memberId();

        memberService.deleteMember(memberId, loginMemberId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("memberId", memberId));
    }
}
