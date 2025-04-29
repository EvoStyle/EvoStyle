package com.example.evostyle.domain.member.dto.request;

public record UpdateAddressRequest(
    String postCode,  // 우편번호 (ex: 12345)
    String fullAddress,  // 도로명 주소 (ex: 테헤란로 123)
    String detailAddress,  // 상세 주소 (ex: 3층 301호)
    String memo  // 참고사항 (ex: 현관 비밀번호는 1234입니다.)
) {
}
