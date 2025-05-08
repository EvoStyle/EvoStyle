package com.example.evostyle.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequest(
    @NotBlank String postCode,  // 우편번호 (ex: 12345)
    @NotBlank String fullAddress,  // 기본 주소 (ex: 서울특별시 강남구 테헤란로 123)
    @NotBlank String detailAddress,  // 상세 주소 (ex: 3층 301호)
    String memo  // 참고사항 (ex: 현관 비밀번호는 1234입니다.)
) {
}
