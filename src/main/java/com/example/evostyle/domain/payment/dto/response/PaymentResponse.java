package com.example.evostyle.domain.payment.dto.response;

import com.example.evostyle.domain.payment.entity.Payment;

public record PaymentResponse(
        String paymentKey,
        String orderId,
        String orderName,
        String method,
        Integer totalAmount
) {

    public static PaymentResponse from(Payment payment){
        return new PaymentResponse(
                payment.getPaymentKey(),
                "orderId - " + payment.getOrder().getId(),
                payment.getOrderName(),
                payment.getMethod(),
                payment.getTotalAmount()
        );
    }
}
