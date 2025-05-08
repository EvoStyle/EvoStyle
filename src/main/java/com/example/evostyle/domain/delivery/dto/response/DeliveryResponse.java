package com.example.evostyle.domain.delivery.dto.response;

import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.DeliveryStatus;
import com.example.evostyle.domain.member.dto.response.MemberResponse;

public record DeliveryResponse(
        MemberResponse memberResponse,
        DeliveryStatus deliveryStatus,
        Long deliveryId,
        String deliveryRequest,
        String address,
        String addressAssistant

) {
    public static DeliveryResponse from(Delivery savedDelivery) {
        return new DeliveryResponse(
                MemberResponse.from(savedDelivery.getMember()),
                savedDelivery.getDeliveryStatus(),
                savedDelivery.getId(),
                savedDelivery.getDeliveryRequest(),
                savedDelivery.getDeliveryAddress(),
                savedDelivery.getDeliveryAddressAssistant()
        );
    }
}
