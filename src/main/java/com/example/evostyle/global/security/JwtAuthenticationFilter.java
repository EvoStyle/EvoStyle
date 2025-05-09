package com.example.evostyle.global.security;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InternalServerException;
import com.example.evostyle.global.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthenticationFilter")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                Claims claims = jwtUtil.parseClaims(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Long memberId = Long.valueOf(claims.getSubject());
                    String roleName = claims.get("authority", String.class);

                    Authority authority;
                    try {
                        authority = Authority.of(roleName);
                    } catch (IllegalArgumentException e) {
                        throw new UnauthorizedException(ErrorCode.JWT_EXCEPTION);
                    }

                    AuthUser authUser = AuthUser.of(memberId, authority);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        authUser,  // principal
                        null,  // credentials (사용 안함)
                        authUser.authorities()
                    );

                    // SecurityContext에 인증 정보 등록
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (ExpiredJwtException e) {
                throw new UnauthorizedException(ErrorCode.EXPIRED_JWT_TOKEN);
            } catch (SignatureException e) {
                throw new UnauthorizedException(ErrorCode.INVALID_JWT_SIGNATURE);
            } catch (MalformedJwtException | SecurityException e) {
                throw new BadRequestException(ErrorCode.MALFORMED_JWT_TOKEN);
            } catch (UnsupportedJwtException e) {
                throw new BadRequestException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(ErrorCode.INVALID_JWT_ARGUMENT);
            } catch (JwtException e) {
                throw new UnauthorizedException(ErrorCode.JWT_EXCEPTION);
            } catch (Exception e) {
                log.error("뭐래"+e.getMessage());
                throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        filterChain.doFilter(request, response);
    }
}
