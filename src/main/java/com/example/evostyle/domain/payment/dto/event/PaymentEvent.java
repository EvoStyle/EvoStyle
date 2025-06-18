package com.example.evostyle.domain.payment.dto.event;

import java.util.List;

public record PaymentEvent(Long memberId,
                           Long orderId,
                           PaymentEventType paymentEventType,
                           List<StockEvent> stockEventList,
                           List<OrderStatusChangeEvent> statusChangeEventList) {

    public static PaymentEvent of(
            Long memberId,
            Long orderId,
            PaymentEventType paymentEventType,
            List<StockEvent> stockEventList,
            List<OrderStatusChangeEvent> statusChangeEventList
    ) {
        return new PaymentEvent(memberId, orderId, paymentEventType, stockEventList, statusChangeEventList);
    }
}
