package com.example.evostyle.domain.payment.dto.request;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Integer amount
) {
}
