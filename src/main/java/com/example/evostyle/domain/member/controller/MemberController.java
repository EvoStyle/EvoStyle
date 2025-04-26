package com.example.evostyle.domain.member.controller;

import com.example.evostyle.domain.member.dto.request.UpdateMemberRequest;
import com.example.evostyle.domain.member.dto.response.MemberResponse;
import com.example.evostyle.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> readMember(
        @PathVariable(name = "memberId") Long memberId,
        HttpServletRequest request
    ) {
        MemberResponse memberResponse = memberService.readMember(memberId, request);

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(
        @PathVariable(name = "memberId") Long memberId,
        @RequestBody UpdateMemberRequest request,
        HttpServletRequest httpServletRequest
    ) {
        MemberResponse memberResponse = memberService.updateMember(memberId, request, httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, Long>> deleteMember(
        @PathVariable(name = "memberId") Long memberId,
        HttpServletRequest request
    ) {
        memberService.deleteMember(memberId, request);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("memberId", memberId));
    }
}
