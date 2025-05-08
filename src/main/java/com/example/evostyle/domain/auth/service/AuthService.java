package com.example.evostyle.domain.auth.service;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.auth.dto.request.LoginRequest;
import com.example.evostyle.domain.auth.dto.request.SignUpRequest;
import com.example.evostyle.domain.auth.dto.response.LoginResponse;
import com.example.evostyle.domain.auth.dto.response.SignUpResponse;
import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public SignUpResponse signup(SignUpRequest request) {
        List<Member> memberList = memberRepository.findDuplicates(request.email(), request.nickname(), request.phoneNumber());

        for (Member member : memberList) {
            if (member.getEmail().equals(request.email())) {
                throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
            }

            if (member.getNickname().equals(request.nickname())) {
                throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
            }

            if (member.getPhoneNumber().equals(request.phoneNumber())) {
                throw new ConflictException(ErrorCode.DUPLICATE_PHONENUMBER);
            }
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

        refreshTokenService.save(member.getId(), refreshToken, jwtUtil.getRefreshTokenExpiration());

        return LoginResponse.from(accessToken, refreshToken);
    }

    public LoginResponse refreshAccessToken(String refreshToken) {
        Claims claims = jwtUtil.parseClaimsAllowExpired(refreshToken);
        Long memberId = Long.valueOf(claims.getSubject());

        String savedToken = refreshTokenService.get(memberId);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = claims.get("email", String.class);
        String nickname = claims.get("nickname", String.class);
        Authority authority = Authority.of(claims.get("authority", String.class));

        String newAccessToken = jwtUtil.createToken(memberId, email, nickname, authority);

        return LoginResponse.from(newAccessToken, refreshToken);
    }

    public void logout(String refreshToken) {
        Claims claims = jwtUtil.parseClaimsAllowExpired(refreshToken);
        Long memberId = Long.valueOf(claims.getSubject());

        refreshTokenService.delete(memberId);
    }
}
