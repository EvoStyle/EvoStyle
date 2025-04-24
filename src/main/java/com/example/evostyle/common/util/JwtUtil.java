package com.example.evostyle.common.util;

import com.example.evostyle.domain.member.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.HashSet;
import java.util.Set;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final long TOKEN_TIME = 60 * 60 * 1000L;
    public static final Set<String> expiredTokenSet = new HashSet<>();

    @Value("NDkxOWJkYzRmNTU3N2RjMGMyZDFlZmM5NDMxODk3ZTUxYjdkZDNkOGZmNzU2YTJiMmQ0ZmNlNzVmYzE1MTRhZA==")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm  signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(secretKey)) {
            log.error("JWT secret key is null or empty");
            throw new IllegalArgumentException("JWT secret key must not be null or empty");
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            key = Keys.hmacShaKeyFor(bytes);
            log.info("JWT secret key initialized successfully");
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode JWT secret key: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT secret key");
        }
    }

    public String createToken(Long memberId, String email, Authority authority) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .claim("email", email)
            .claim("Authority", authority)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm).compact();
    }

    public String substringTokens(String tokenValue) {
        if (!StringUtils.hasText(tokenValue)) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }
        if (!tokenValue.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("Token does not start with Bearer");
        }
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }

        throw new IllegalArgumentException("Not found Token");
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = extractClaims(token);
        return Long.parseLong(claims.getSubject());
    }
}
