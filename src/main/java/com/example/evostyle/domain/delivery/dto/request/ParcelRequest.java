package com.example.evostyle.domain.delivery.dto.request;

public record ParcelRequest(
       SenderRequest senderRequest,
       ReceiverRequest receiverRequest,
       String deliveryRequest
) {
    public static ParcelRequest of(SenderRequest senderRequest, ReceiverRequest receiverRequest, String deliveryRequest) {
        return new ParcelRequest(senderRequest, receiverRequest,deliveryRequest);
    }
}
