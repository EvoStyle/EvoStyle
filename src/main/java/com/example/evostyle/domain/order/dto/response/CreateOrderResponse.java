package com.example.evostyle.domain.order.dto.response;

import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        List<CreateOrderItemResponse> orderItemResponseList,
        Integer totalAmountSum,
        Integer totalPriceSum
) {
    public static CreateOrderResponse from(
            Long orderId,
            List<CreateOrderItemResponse> orderItemResponseList,
            Integer totalAmountSum,
            Integer totalPriceSum
    ) {
        return new CreateOrderResponse(
                orderId,
                orderItemResponseList,
                totalAmountSum,
                totalPriceSum
        );
    }
}
