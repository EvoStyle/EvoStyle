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
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    DUPLICATE_PHONENUMBER(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자입니다."),

    // JWT 관련
    MISSING_JWT_SECRET_KEY(HttpStatus.BAD_REQUEST, "JWT 시크릿 키가 누락되어 토큰 생성을 할 수 없습니다."),
    INVALID_JWT_SECRET_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 시크릿 키 디코딩에 실패했습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "Authorization 헤더의 Bearer 토큰이 유효하지 않습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    MALFORMED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰 형식입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST,"지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰 값입니다."),
    JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "JWT 토큰 처리 중 오류가 발생했습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    //서버 관련
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    //주소 관련
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다"),

    //브랜드 관련
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드입니다"),
    BRAND_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 브랜드 이름입니다."),
    NON_EXISTENT_BRAND_CATEGORY(HttpStatus.BAD_REQUEST, "요청한 카테고리 중 유효한 항목이 없습니다."),
    CATEGORY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "브랜드 카테고리는 최대 3개까지입니다."),
    NOT_BRAND_OWNER(HttpStatus.FORBIDDEN, "해당 브랜드 관련 권한이 없습니다"),
    BRAND_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 브랜드 카테고리입니다."),

    //주문 관련
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 총 주문입니다."),
    ORDER_ITEM_NOT_PENDING(HttpStatus.BAD_REQUEST, "존재하지 않거나 수락 대기 상태가 아닌 주문 세부 사항입니다."),
    ORDER_GET_FORBIDDEN(HttpStatus.FORBIDDEN, "다른 사용자의 주문 내역은 조회할 수 없습니다."),
    ORDER_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "이 주문을 취소할 권한이 없습니다."),
    ALREADY_CANCELLED_ORDER(HttpStatus.BAD_REQUEST, "이미 취소처리 된 주문입니다."),

    //주문세부사항 관련
    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 세부사항이 존재하지 않습니다."),

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

    // 주문 관련
    INVALID_STOCK_DECREASE_AMOUNT(HttpStatus.BAD_REQUEST, "재고 차감 수량은 0보다 커야 합니다"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),

    //리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다"),
    REVIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN, "리뷰는 배송이 완료된 상품에 대해서만 작성 가능합니다."),
    NOT_OWNER_OF_ORDER(HttpStatus.FORBIDDEN, "해당 주문에 대한 권한이 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 해당 주문 상품에 대한 리뷰가 작성되었습니다."),

    //즐겨찾기 관련
    BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 즐겨찾기입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "즐겨찾기가 존재하지 않습니다."),

    //배송 관련
    DELIVERY_NOT_READY(HttpStatus.BAD_REQUEST,"배송이 이미 시작되었습니다. 택배회사에 문의해주세요"),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송입니다."),
    PARCEL_API_FAIL(HttpStatus.UNPROCESSABLE_ENTITY, "택배 송장 등록에 실패했습니다."),
    DELIVERY_CONFLICT_MODIFIED_BY_ADMIN(HttpStatus.CONFLICT,"다른 관리자가 해당 배송을 출고하였습니다."),
    DELIVERY_CONFLICT_MODIFIED_BY_USER(HttpStatus.CONFLICT,"사용자가 배송 정보를 수정하였습니다. 출고 전에 다시 확인해 주세요."),

    //직렬화 관련
    JSON_SERIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 직렬화 실패"),

    //쿠폰 관련
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_ALREADY_ISSUED(HttpStatus.CONFLICT, "이미 발급받은 쿠폰입니다."),
    COUPON_ISSUE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "쿠폰 발급 수량을 초과했습니다."),

    //Lock 관련
    LOCK_ACQUISITION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "잠금 획득에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
