package com.example.evostyle.domain.order.entity;

import java.util.Arrays;

public enum OrderStatus {
    PENDING, // 주문 생성 시, 즉 수락 대기 상태
    ACCEPTED, // 주문 수락
    CANCELED, // 주문 취소
    DELIVERING, // ACCEPTED 이후 배송 중
    DELIVERED; // DELIVERING 이후 배송 완료

    // todo 예외 처리 필요
    // todo 입력된 문자열을 바탕으로 OrderStatus 값을 찾는 메서드 (필요 없을 시 삭제 예정)
    public static OrderStatus of(String orderState) {
        return Arrays.stream(OrderStatus.values())
                .filter(
                        status -> status
                                .name()
                                .equalsIgnoreCase(orderState))
                .findFirst()
                .orElseThrow();
    }
}
