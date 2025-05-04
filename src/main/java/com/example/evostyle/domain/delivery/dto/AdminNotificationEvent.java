package com.example.evostyle.domain.delivery.dto;

import com.example.evostyle.domain.delivery.entity.Delivery;

import java.time.LocalDateTime;

public record AdminNotificationEvent(
        Long deliveryId,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String address,
        String addressAssistant,
        String deliveryRequest,
        String trackingNumber,
        String memberNickname,
        String phoneNumber,
        boolean success,
        String message,
        String slackWebHookUrl
) {
    public static AdminNotificationEvent success(AdminDeliveryResponse adminDeliveryResponse) {
        return new AdminNotificationEvent(
                adminDeliveryResponse.deliveryId(),
                adminDeliveryResponse.createAt(),
                adminDeliveryResponse.updateAt(),
                adminDeliveryResponse.address(),
                adminDeliveryResponse.addressAssistant(),
                adminDeliveryResponse.deliveryRequest(),
                adminDeliveryResponse.trackingNumber(),
                adminDeliveryResponse.memberNickname(),
                adminDeliveryResponse.phoneNumber(),
                true,
                "배송 출고 등록 완료",
                adminDeliveryResponse.slackWebHookUrl()
        );
    }

    public static AdminNotificationEvent fail(Delivery delivery) {
        return new AdminNotificationEvent(
                delivery.getId(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                delivery.getDeliveryAddress(),
                delivery.getDeliveryAddressAssistant(),
                delivery.getDeliveryRequest(),
                delivery.getTrackingNumber(),
                delivery.getMember().getNickname(),
                delivery.getMember().getPhoneNumber(),
                false,
                "배송이 이미 출고 되었습니다."
        );
    }
}
