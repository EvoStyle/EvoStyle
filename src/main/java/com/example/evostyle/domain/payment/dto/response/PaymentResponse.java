package com.example.evostyle.domain.payment.dto.response;

public record PaymentResponse(
        String paymentKey,
        String method,
        Integer totalAmount
) {

    public static PaymentResponse from(TossPaymentResponse paymentResponse) {
        return new PaymentResponse(
                paymentResponse.paymentKey(),
                paymentResponse.method(),
                paymentResponse.totalAmount()
        );
    }
}
