package com.example.evostyle.domain.payment.dto.response;

import java.time.LocalDateTime;

public record TossPaymentResponse(
        String orderId,
        String paymentKey,
        String method,
        String orderName,
        Integer totalAmount,
        LocalDateTime approvedAt
) {
}
