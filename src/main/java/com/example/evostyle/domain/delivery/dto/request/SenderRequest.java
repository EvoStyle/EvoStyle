package com.example.evostyle.domain.delivery.dto.request;

public record SenderRequest(
        String name
) {
    public static SenderRequest of(String name) {
        return new SenderRequest(name);
    }
}
