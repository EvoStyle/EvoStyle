package com.example.evostyle.domain.payment.dto.request;

import com.example.evostyle.domain.order.entity.OrderItem;
import jakarta.annotation.Nullable;

import java.util.List;

public record PaymentCancelRequest(String paymentKey, @Nullable Integer cancelAmount, List<OrderItem> cancelOrderItemList) {
    public static PaymentCancelRequest of(String paymentKey, @Nullable Integer cancelAmount, List<OrderItem> cancelOrderItemList){
        return new PaymentCancelRequest(paymentKey, cancelAmount, cancelOrderItemList);
    }
}
