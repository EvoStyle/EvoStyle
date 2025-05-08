package com.example.evostyle.common.util;

import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.global.exception.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000;  // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;  // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(secretKey)) {
            log.error("JWT secret key is null or empty.");
            throw new BadRequestException(ErrorCode.MISSING_JWT_SECRET_KEY);
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            key = Keys.hmacShaKeyFor(bytes);
            log.info("JWT secret key initialized successfully");
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode JWT secret key: {}", e.getMessage());
            throw new InternalServerException(ErrorCode.INVALID_JWT_SECRET_KEY);
        }
    }

    // Access Token 생성
    public String createToken(Long memberId, String email, String nickname, Authority authority) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .claim("email", email)
            .claim("nickname", nickname)
            .claim("authority", authority.getRoleName())  // "ROLE_OWNER" 같은 문자열로 저장
            .setIssuedAt(new Date())
            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRATION))
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long memberId) {
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            token = removeBearer(token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    // 내부적으로 claim 파싱 처리
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_JWT_TOKEN);
        }
    }

    // 만료된 Refresh Token에서 memberId 추출
    public Claims parseClaimsAllowExpired(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(removeBearer(token))
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();  // 만료되었어도 Claims는 꺼낼 수 있음
        }
    }

    // 토큰에서 사용자 Id 추출
    public Long getMemberId(String token) {
        token = removeBearer(token);
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    // 토큰에서 이메일 추출
    public String getEmail(String token) {
        token = removeBearer(token);
        Claims claims = parseClaims(token);
        return claims.get("email", String.class);
    }

    // 토큰에서 회원 권한 추출
    public String getAuthority(String token) {
        token = removeBearer(token);
        Claims claims = parseClaims(token);
        return claims.get("authority", String.class);
    }

    public long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_EXPIRATION;
    }

    // "Bearer " 접두어 제거
    private String removeBearer(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }

        return token;
    }
}
