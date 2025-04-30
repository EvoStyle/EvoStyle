package com.example.evostyle.domain.parcel.dto.request;

public record ParcelRequest(
       SenderRequest senderRequest,
       ReceiverRequest receiverRequest
) {

}
