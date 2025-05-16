package com.example.evostyle.domain.payment.dto.response;

import com.example.evostyle.domain.payment.entity.Payment;

public record PaymentResponse(
        String paymentKey,
        String orderId,
        String orderName,
        String method,
        Integer totalAmount
) {

    public static PaymentResponse from(TossPaymentResponse response) {
        return new PaymentResponse(
                response.paymentKey(),
                "order-" + response.orderId(),
                response.orderName(),
                response.method(),
                response.totalAmount()
        );
    }
}
