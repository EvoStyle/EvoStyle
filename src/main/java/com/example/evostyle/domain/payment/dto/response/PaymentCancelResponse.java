package com.example.evostyle.domain.payment.dto.response;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record PaymentCancelResponse(String paymentKey,
                                    String method,
                                    Integer totalAmount,
                                    LocalDateTime approvedAt,
                                    LocalDateTime canceledAt
) {
    public static PaymentCancelResponse from(TossPaymentResponse tossCancelPaymentResponse) {
        LocalDateTime canceledAt = tossCancelPaymentResponse.canceledAt() == null ? null
                :OffsetDateTime.parse(tossCancelPaymentResponse.canceledAt()).toLocalDateTime();

        return new PaymentCancelResponse(tossCancelPaymentResponse.paymentKey(),
                tossCancelPaymentResponse.method(),
                tossCancelPaymentResponse.totalAmount(),
                OffsetDateTime.parse(tossCancelPaymentResponse.approvedAt()).toLocalDateTime(),
                canceledAt
        );
    }
}
