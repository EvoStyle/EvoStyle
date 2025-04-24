package com.example.evostyle.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //회원 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    INVALID_MEMBER_AUTHORITY(HttpStatus.BAD_REQUEST, "유효하지 않은 Authority 입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자입니다."),

    //주소 관련
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다"),

    //브랜드 관련
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다"),

    //주문 관련
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    ORDER_GET_FORBIDDEN(HttpStatus.FORBIDDEN,"다른 사용자의 주문 내역은 조회할 수 없습니다."),
    ORDER_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN,"이 주문을 취소할 권한이 없습니다."),
    ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소처리 된 주문입니다."),

    //상품 관련
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),

    //상품 카테고리 관련
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 카테고리가 존재하지 않습니다"),

    //상품 옵션그룹 관련
    OPTION_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션 그룹이 존재하지 않습니다"),


    //리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다");



    private final HttpStatus httpStatus;
    private final String message;

}
