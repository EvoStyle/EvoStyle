package com.example.evostyle.domain.payment.dto.event;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;

public record PaymentConfirmEvent(Long memberId, Long orderId, TossPaymentResponse paymentResponse) {

    public static PaymentConfirmEvent from(Order order, TossPaymentResponse paymentResponse){
        return new PaymentConfirmEvent(order.getMember().getId(), order.getId(), paymentResponse);
    }
}
