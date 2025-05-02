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


    // JWT 관련
    MISSING_JWT_SECRET_KEY(HttpStatus.BAD_REQUEST, "JWT 시크릿 키가 누락되어 토큰 생성을 할 수 없습니다."),
    INVALID_JWT_SECRET_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 시크릿 키 디코딩에 실패했습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "Authorization 헤더의 Bearer 토큰이 유효하지 않습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),

    //주소 관련
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다"),

    //브랜드 관련
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다"),
    BRAND_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 브랜드 이름입니다."),
    NON_EXISTENT_BRAND_CATEGORY(HttpStatus.BAD_REQUEST, "요청한 카테고리 중 유효한 항목이 없습니다."),
    CATEGORY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "브랜드 카테고리는 최대 3개까지입니다."),
    BRAND_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드 카테고리입니다."),

    //주문 관련
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    ORDER_GET_FORBIDDEN(HttpStatus.FORBIDDEN, "다른 사용자의 주문 내역은 조회할 수 없습니다."),
    ORDER_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "이 주문을 취소할 권한이 없습니다."),
    ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소처리 된 주문입니다."),

    //상품 관련
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
    PRODUCT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품 세부사항입니다"),

    //장바구니  관련
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장바구니 상품입니다"),
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "본인의 장바구니 정보만 수정 가능합니다"),
    CART_ITEM_ALREADY_EXISTS(HttpStatus.CONFLICT, "장바구니에 존재하는 상품입니다"),

    //상품 카테고리 관련
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 카테고리가 존재하지 않습니다"),

    //상품 옵션그룹 관련
    OPTION_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션 그룹이 존재하지 않습니다"),
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션이 존재하지 않습니다"),

    // 주문 관련
    INVALID_STOCK_DECREASE_AMOUNT(HttpStatus.BAD_REQUEST, "재고 차감 수량은 0보다 커야 합니다"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),

    //리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다"),

    //딜리버리 관련
    DELIVERY_NOT_READY(HttpStatus.BAD_REQUEST,"배송이 준비상태가 아닙니다."),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송입니다."),
  
    // 즐겨찾기 관련
    BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 즐겨찾기입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "즐겨찾기가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
