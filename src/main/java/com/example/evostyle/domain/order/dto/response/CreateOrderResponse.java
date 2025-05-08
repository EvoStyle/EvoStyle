package com.example.evostyle.domain.order.dto.response;

import com.example.evostyle.domain.order.entity.Order;

import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        List<CreateOrderItemResponse> orderItemResponseList,
        Integer totalAmountSum,
        Integer totalPriceSum
) {
    public static CreateOrderResponse from(
            Order order,
            List<CreateOrderItemResponse> orderItemResponseList
    ) {
        return new CreateOrderResponse(
                order.getId(),
                orderItemResponseList,
                order.getTotalAmountSum(),
                order.getTotalPriceSum()
        );
    }
}
