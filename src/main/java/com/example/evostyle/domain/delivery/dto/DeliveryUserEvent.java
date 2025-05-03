package com.example.evostyle.domain.delivery.dto;

public record DeliveryUserEvent(
        Long deliveryId,
        Long userId,
        Long addressId,
        String newDeliveryRequest
) {
}
