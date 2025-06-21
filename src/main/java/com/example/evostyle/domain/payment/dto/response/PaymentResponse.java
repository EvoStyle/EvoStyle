package com.example.evostyle.domain.payment.dto.response;

import com.example.evostyle.domain.payment.entity.Payment;

public record PaymentResponse(
        String paymentKey,
        String method,
        Integer totalAmount
) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentKey(),
                payment.getMethod(),
                payment.getTotalAmount()
        );
    }
}
