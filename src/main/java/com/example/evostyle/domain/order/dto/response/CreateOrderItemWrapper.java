package com.example.evostyle.domain.order.dto.response;

import java.util.List;

public record CreateOrderItemWrapper(
        Long orderId,
        List<CreateOrderItemResponse> orderItemResponseList,
        Integer totalAmountSum,
        Integer totalPriceSum
) {
    public static CreateOrderItemWrapper from(
            Long orderId,
            List<CreateOrderItemResponse> orderItemResponseList,
            Integer totalAmountSum,
            Integer totalPriceSum
    ) {
        return new CreateOrderItemWrapper(
                orderId,
                orderItemResponseList,
                totalAmountSum,
                totalPriceSum
        );
    }
}
