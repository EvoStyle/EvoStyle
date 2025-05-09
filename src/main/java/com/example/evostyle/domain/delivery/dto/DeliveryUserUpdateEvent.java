package com.example.evostyle.domain.delivery.dto;

public record DeliveryUserUpdateEvent(
        EventType eventType,
        Long userId,
        Long deliveryId,
        Long addressId,
        String newDeliveryRequest
) {
    public static DeliveryUserUpdateEvent of(EventType eventType,Long userId,Long deliveryId, Long addressId, String newDeliveryRequest) {
        return new DeliveryUserUpdateEvent(eventType,userId,deliveryId, addressId , newDeliveryRequest);
    }
}
