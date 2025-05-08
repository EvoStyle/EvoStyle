package com.example.evostyle.domain.payment.dto.request;

public record PaymentCancelRequest(String cancelReason) {

    public static PaymentCancelRequest of(String cancelReason){
        return new PaymentCancelRequest(cancelReason);
    }
}
