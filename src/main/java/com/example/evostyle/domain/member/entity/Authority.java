package com.example.evostyle.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Authority {

    CUSTOMER("ROLE_CUSTOMER"),
    OWNER("ROLE_OWNER");

    private final String roleName;

    public static Authority of(String role) {
        return Arrays.stream(Authority.values())
            .filter(r -> r.name().equalsIgnoreCase(role) || r.getRoleName().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userRole 입니다."));
    }
}
