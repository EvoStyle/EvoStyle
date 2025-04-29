package com.example.evostyle.domain.order.dto.response;

import java.util.List;

public record ReadOrderResponse(
        Long orderId,
        List<ReadOrderItemResponse> orderItemResponseList,
        Integer totalAmountSum,
        Integer totalPriceSum
) {
    public static ReadOrderResponse from (
            Long orderId,
            List<ReadOrderItemResponse> orderItemResponseList,
            Integer totalAmountSum,
            Integer totalPriceSum
    ) {
        return new ReadOrderResponse(
                orderId,
                orderItemResponseList,
                totalAmountSum,
                totalPriceSum
        );
    }
}
