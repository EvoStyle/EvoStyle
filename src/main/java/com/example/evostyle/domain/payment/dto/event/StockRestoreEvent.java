package com.example.evostyle.domain.payment.dto.event;

public record StockRestoreEvent(Long orderId) {
    public static StockRestoreEvent from(Long orderId){
        return new StockRestoreEvent(orderId);
    }
}
