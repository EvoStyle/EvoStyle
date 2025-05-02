package com.example.evostyle.domain.parcel.dto.request;

public record ReceiverRequest(
        String name,
        String address,
        String phone,
        String postCode
) {
}
