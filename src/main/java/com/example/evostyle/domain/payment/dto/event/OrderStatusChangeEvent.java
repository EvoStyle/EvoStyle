package com.example.evostyle.domain.payment.dto.event;

import com.example.evostyle.domain.order.entity.OrderStatus;

public record OrderStatusChangeEvent(Long orderItemId, OrderStatus orderStatus) {

    public static OrderStatusChangeEvent of(Long orderItemId, OrderStatus orderStatus){
        return new OrderStatusChangeEvent(orderItemId, orderStatus);
    }
}
