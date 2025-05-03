package com.example.evostyle.domain.delivery.dto;

public record DeliveryAdminUpdateEvent(
        EventType eventType,
        Long deliveryId
) {
    public static DeliveryAdminUpdateEvent of(EventType eventType, Long deliveryId) {
        return new DeliveryAdminUpdateEvent(eventType, deliveryId);
    }
}
