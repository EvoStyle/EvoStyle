package com.example.evostyle.domain.delivery.dto;

public record UserNotificationEvent(
        Long userId,
        Long deliveryId,
        boolean success,
        String message
) {
}
