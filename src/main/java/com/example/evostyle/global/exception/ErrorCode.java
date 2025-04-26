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
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "올바르지 않은 비밀번호입니다."),
    FORBIDDEN_MEMBER_OPERATION(HttpStatus.FORBIDDEN, "해당 회원에 대한 작업 권한이 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자입니다."),

    //주소 관련
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다"),

    //브랜드 관련
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다"),
    BRAND_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 브랜드 이름입니다."),
    NON_EXISTENT_BRAND_CATEGORY(HttpStatus.BAD_REQUEST, "요청한 카테고리 중 유효한 항목이 없습니다."),
    CATEGORY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "브랜드 카테고리는 최대 3개까지입니다."),
    BRAND_CATEGORY_MAPPING_NOT_FOUND(HttpStatus.NOT_FOUND, "요청된 브랜드와 카테고리의 매핑을 찾을 수 없습니다."),

    //주문 관련
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    ORDER_GET_FORBIDDEN(HttpStatus.FORBIDDEN, "다른 사용자의 주문 내역은 조회할 수 없습니다."),
    ORDER_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "이 주문을 취소할 권한이 없습니다."),
    ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소처리 된 주문입니다."),

    //상품 관련
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),

    //상품 카테고리 관련
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 카테고리가 존재하지 않습니다"),

    //상품 옵션그룹 관련
    OPTION_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션 그룹이 존재하지 않습니다"),

    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션이 존재하지 않습니다"),


    //리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다");

    private final HttpStatus httpStatus;
    private final String message;

}
