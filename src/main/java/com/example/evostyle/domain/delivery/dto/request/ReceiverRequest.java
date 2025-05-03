package com.example.evostyle.domain.delivery.dto.request;

public record ReceiverRequest(
        String name,
        String address,
        String addressAssistant,
        String phone,
        String postCode
) {

    public static ReceiverRequest of(String name, String address, String addressAssistant,String phone, String postCode) {
        return new ReceiverRequest(name, address, addressAssistant,phone, postCode);
    }
}
