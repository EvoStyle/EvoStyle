package com.example.evostyle.domain.payment.dto.event;

import java.util.List;

public record PaymentEvent(Long memberId, List<Long> orderItemIdList, PaymentEventType paymentEventType) {

    public static PaymentEvent of(
            Long memberId,
            List<Long> orderItemIdList,
            PaymentEventType paymentEventType
    ) {
        return new PaymentEvent(memberId, orderItemIdList, paymentEventType);
    }
}
