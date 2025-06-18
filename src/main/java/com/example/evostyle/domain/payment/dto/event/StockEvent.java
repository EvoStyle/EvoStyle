package com.example.evostyle.domain.payment.dto.event;

import com.example.evostyle.domain.order.entity.OrderItem;

public record StockEvent(Long orderItemId, Long productDetailId, OperationType operationType) {
    public static StockEvent of(OrderItem orderItem, OperationType operationType){
        return new StockEvent(
                orderItem.getId(), orderItem.getProductDetail().getId(), operationType);
    }
}
