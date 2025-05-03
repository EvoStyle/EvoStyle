package com.example.evostyle.global.security;

import com.example.evostyle.domain.member.entity.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record AuthUser(
    Long memberId,
    Collection<? extends GrantedAuthority> authorities
) {
    public static AuthUser of(Long memberId, Authority authority) {
        return new AuthUser(
            memberId,
            List.of(new SimpleGrantedAuthority(authority.getRoleName()))
        );
    }
}
