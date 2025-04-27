package com.example.evostyle.global.filter;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.UnauthorizedException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j(topic = "JwtFilter")
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpServletRequest.getRequestURI();

        // 회원가입, 로그인 URI 로 들어오면 통과
        if (requestURI.startsWith("/api/auth/")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String bearerJwt = httpServletRequest.getHeader("Authorization");

        // 요청 헤더에 Authorization 이 없거나, Bearer 로 시작하지 않으면 예외 처리
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            log.error("Invalid Authorization header on URI: {}", requestURI);
            throw new UnauthorizedException(ErrorCode.INVALID_BEARER_TOKEN);
        }

        String token = bearerJwt.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException(ErrorCode.INVALID_JWT_TOKEN);
        }

        Long memberId = jwtUtil.getMemberId(token);
        String email = jwtUtil.getEmail(token);
        String authority = jwtUtil.getAuthority(token);

        httpServletRequest.setAttribute("memberId", memberId);
        httpServletRequest.setAttribute("email", email);
        httpServletRequest.setAttribute("authority", authority);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
