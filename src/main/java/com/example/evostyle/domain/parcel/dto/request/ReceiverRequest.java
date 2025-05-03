package com.example.evostyle.domain.parcel.dto.request;

public record ReceiverRequest(
        String name,
        String address,
        String addressAssistant,
        String phone,
        String postCode
) {
}
