package com.example.evostyle.domain.auth.dto.request;

import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.GenderType;
import jakarta.validation.constraints.*;

public record SignUpRequest(
    @Email(message = "이메일 형식이 올바르지 않습니다.") String email,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "비밀번호는 소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
    @NotBlank String password,
    @NotBlank String nickname,
    @NotNull @Positive(message = "나이는 양수여야 합니다.") Integer age,
    @NotBlank String phoneNumber,
    @NotNull Authority authority,
    @NotNull GenderType genderType
) {
}
