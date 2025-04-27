package com.example.evostyle.domain.address.dto.request;

public record UpdateAddressRequest(
    String postCode,  // 우편번호 (ex: 12345)
    String siDo,  // 시/도 (ex: 서울특별시)
    String siGunGu,  // 시/군/구 (ex: 강남구)
    String roadNameAddress,  // 도로명 주소 (ex: 테헤란로 123)
    String detailAddress,  // 상세 주소 (ex: 3층 301호)
    String extraAddress  // 선택사항 (ex: 삼성타워)
) {
}
