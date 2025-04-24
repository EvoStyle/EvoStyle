package com.example.evostyle.domain.auth.dto.request;

import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.GenderType;

public record SignUpRequest(String email, String password, String nickname, Integer age,
                            String phoneNumber, Authority authority, GenderType genderType) {
}
