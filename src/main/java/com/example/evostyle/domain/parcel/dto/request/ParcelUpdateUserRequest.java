package com.example.evostyle.domain.parcel.dto.request;

import com.example.evostyle.domain.member.entity.Address;

public record ParcelUpdateUserRequest(
        String address,
        String addressAssistant,
        String postCode,
        String deliveryRequest

) {
    public static ParcelUpdateUserRequest of(Address address, String newDeliveryRequest) {
        return new ParcelUpdateUserRequest(address.getFullAddress(), address.getDetailAddress(),address.getPostCode(),newDeliveryRequest);
    }
}
