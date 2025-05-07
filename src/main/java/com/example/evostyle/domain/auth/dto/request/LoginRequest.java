package com.example.evostyle.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank
    String email,
    @NotBlank
    String password
) {
}
