package com.example.evostyle.domain.delivery.dto;

public record DeliveryEventWrapper(
        EventType eventType,
        Object payload
) {
}
