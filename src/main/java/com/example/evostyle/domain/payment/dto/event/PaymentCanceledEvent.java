package com.example.evostyle.domain.payment.dto.event;

public record PaymentCanceledEvent(Long memberId, Long orderId, String paymentKey) {

    public static PaymentCanceledEvent of(Long memberId, Long orderId, String paymentKey){
        return new PaymentCanceledEvent(memberId, orderId, paymentKey);
    }
}
