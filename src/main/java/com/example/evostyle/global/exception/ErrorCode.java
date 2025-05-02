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
    CATEGORY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "브랜드 카테고리는 최대 3개까지입니다."),
    NOT_BRAND_OWNER(HttpStatus.FORBIDDEN, "해당 브랜드 관련 권한이 없습니다"),

    //주문 관련
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    ORDER_GET_FORBIDDEN(HttpStatus.FORBIDDEN, "다른 사용자의 주문 내역은 조회할 수 없습니다."),
    ORDER_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "이 주문을 취소할 권한이 없습니다."),
    ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소처리 된 주문입니다."),

    //상품 관련
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),

    //상품 디테일 관련
    STOCK_MODIFICATION_NOT_ALLOWED(HttpStatus.CONFLICT, "재고를 수정할 수 없는 상태입니다"),
    PRODUCT_DETAIL_MISMATCH(HttpStatus.CONFLICT, "해당 상품에 속하지 않는 상품 디테일입니다"),

    //상품 카테고리 관련
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 카테고리가 존재하지 않습니다"),

    //상품 옵션그룹 관련
    OPTION_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션 그룹이 존재하지 않습니다"),
    //상품옵션 관련
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션이 존재하지 않습니다"),
    INVALID_PRODUCT_OPTION(HttpStatus.BAD_REQUEST, "해당 상품의 옵션이 아닙니다"),
    MULTIPLE_OPTION_SELECTED(HttpStatus.BAD_REQUEST, "하나의 옵션그룹에서는 하나의 옵션만 선택할 수 있습니다."),


    //상품디테일 옵션 관련
    PRODUCT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 상세옵션이 존재하지 않습니다"),

    //리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다");

    private final HttpStatus httpStatus;
    private final String message;

}
