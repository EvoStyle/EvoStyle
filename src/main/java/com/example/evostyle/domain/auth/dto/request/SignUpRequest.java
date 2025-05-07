package com.example.evostyle.domain.auth.dto.request;

import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.GenderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
    @Email(message = "이메일 형식이 올바르지 않습니다.") String email,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "올바른 비밀번호가 아닙니다.") @NotBlank String password,
    @NotBlank String nickname,
    @NotBlank Integer age,
    @NotBlank String phoneNumber,
    @NotBlank Authority authority,
    @NotBlank GenderType genderType
) {
}
