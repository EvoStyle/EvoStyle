package com.example.evostyle.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMemberUtil {

    public static Long getMemberId(HttpServletRequest request, JwtUtil jwtUtil) {
        String token = extractToken(request);

        return jwtUtil.getMemberIdFromToken(token);
    }

    private static String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if(!StringUtils.hasText((bearer)) || !bearer.startsWith(JwtUtil.BEARER_PREFIX)) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더입니다.");
        }

        return bearer.substring(JwtUtil.BEARER_PREFIX.length());
    }
}
