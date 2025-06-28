package com.example.evostyle.domain.payment.dto.request;

public record PaymentCancelRequest(String paymentKey, String cancelReason) {
    public static PaymentCancelRequest of(String paymentKey, String cancelReason){
        return new PaymentCancelRequest(paymentKey, cancelReason);
    }
}
