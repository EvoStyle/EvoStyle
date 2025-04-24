package com.example.evostyle.domain.auth.dto.response;

public record LoginResponse(String accessToken) {

    public static LoginResponse of(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
