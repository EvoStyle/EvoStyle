package com.example.evostyle.domain.delivery.dto;

public record DeliveryUserEvent(
        Long deliveryId,
        Long addressId,
        String newDeliveryRequest
) {
}
