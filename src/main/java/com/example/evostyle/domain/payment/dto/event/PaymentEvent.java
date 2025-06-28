package com.example.evostyle.domain.payment.dto.event;

import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record PaymentEvent(Long orderId,
                           OrderStatus status,
                           PaymentStatus paymentStatus,
                           String paymentKey,
                           Integer totalAmount,
                           String method,
                           LocalDateTime approvedAt,
                           @Nullable LocalDateTime canceledAt) {

    public static PaymentEvent of(Long orderId, OrderStatus status, PaymentStatus paymentStatus, TossPaymentResponse paymentResponse){

        LocalDateTime canceledAt = paymentResponse.canceledAt() == null ? null
                : OffsetDateTime.parse(paymentResponse.canceledAt()).toLocalDateTime();

        return new PaymentEvent(orderId,
                status,
                paymentStatus,
                paymentResponse.paymentKey(),
                paymentResponse.totalAmount(),
                paymentResponse.method(),
                OffsetDateTime.parse(paymentResponse.approvedAt()).toLocalDateTime(),
                canceledAt
        );
    }
}
