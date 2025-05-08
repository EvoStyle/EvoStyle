package com.example.evostyle.domain.payment.dto.response;

public record TossPaymentResponse(
        String orderId,
        String paymentKey,
        String method,
        String orderName,
        Integer totalAmount,
        String approvedAt
) {
}
