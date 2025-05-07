package com.example.evostyle.domain.auth.service;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.auth.dto.request.LoginRequest;
import com.example.evostyle.domain.auth.dto.request.SignUpRequest;
import com.example.evostyle.domain.auth.dto.response.LoginResponse;
import com.example.evostyle.domain.auth.dto.response.SignUpResponse;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signup(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (memberRepository.existsByNickname(request.nickname())) {
            throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
        }

        if (memberRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ConflictException(ErrorCode.DUPLICATE_PHONENUMBER);
        }

        Member member = Member.of(
            request.email(),
            passwordEncoder.encode(request.password()),
            request.nickname(),
            request.age(),
            request.phoneNumber(),
            request.authority(),
            request.genderType()
        );

        memberRepository.save(member);

        return SignUpResponse.from(member);
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createToken(
            member.getId(),
            member.getEmail(),
            member.getNickname(),
            member.getAuthority());

        String refreshToken = jwtUtil.createRefreshToken(member.getId());

        return LoginResponse.from(accessToken, refreshToken);
    }
}
