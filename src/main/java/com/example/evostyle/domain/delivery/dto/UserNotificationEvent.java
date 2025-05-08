package com.example.evostyle.domain.delivery.dto;

public record UserNotificationEvent(
        Long userId,
        Long deliveryId,
        String trackingNumber,
        boolean success,
        String message
) {
    public static UserNotificationEvent success(Long userId, Long deliveryId) {
        return new UserNotificationEvent(userId,deliveryId,null,true,"배송 정보 변경이 성공적으로 처리되었습니다.");
    }

    public static UserNotificationEvent fail(Long userId, String trackingNumber) {
        return new UserNotificationEvent(userId,null, trackingNumber, false, "배송 정보 변경이 실패하였습니다.");
    }
}
