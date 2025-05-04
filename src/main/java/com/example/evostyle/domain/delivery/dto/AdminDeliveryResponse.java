package com.example.evostyle.domain.delivery.dto;

import com.example.evostyle.domain.delivery.entity.Delivery;

import java.time.LocalDateTime;

public record AdminDeliveryResponse(
        Long deliveryId,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String address,
        String addressAssistant,
        String deliveryRequest,
        String trackingNumber,
        String memberNickname,
        String phoneNumber,
        String slackWebHookUrl
) {
    public static AdminDeliveryResponse from(Delivery delivery) {
        return new AdminDeliveryResponse(
                delivery.getId(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                delivery.getDeliveryAddress(),
                delivery.getDeliveryAddressAssistant(),
                delivery.getDeliveryRequest(),
                delivery.getTrackingNumber(),
                delivery.getMember().getNickname(),
                delivery.getMember().getPhoneNumber(),
                delivery.getBrand().getSlackWebHookUrl()
        );
    }
}
