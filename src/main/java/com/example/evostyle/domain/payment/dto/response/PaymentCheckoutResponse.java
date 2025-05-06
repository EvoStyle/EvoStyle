package com.example.evostyle.domain.payment.dto.response;

import com.example.evostyle.domain.order.entity.Order;

public record PaymentCheckoutResponse(
        String orderId,
        String orderName,
        Integer totalAmount,
        String customerName,
        String customerEmail,
        String customerKey
) {

    public static PaymentCheckoutResponse from(Order order){

      String orderName = order.getOrderItemList().size() == 1 ? order.getOrderItemList().get(0).getProductName() :
              order.getOrderItemList().get(0).getProductName() + "외" + (order.getOrderItemList().size() - 1) + "개";

        return new PaymentCheckoutResponse(
               "orderId - " + order.getId(),
                orderName,
                order.getTotalAmountSum(),
                order.getMember().getNickname(),
                order.getMember().getEmail(),
                "user - " + order.getMember().getId()
        );
    }
}
