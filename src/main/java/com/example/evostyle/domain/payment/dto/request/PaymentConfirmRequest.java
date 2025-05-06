package com.example.evostyle.domain.payment.dto.request;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Integer amount
) {

    public static PaymentConfirmRequest of(String paymentKey,  String orderId, Integer amount){
        return new PaymentConfirmRequest(paymentKey, orderId, amount);
    }
}
