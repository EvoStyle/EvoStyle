package com.example.evostyle.domain.payment.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.member.service.MemberService;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.service.OrderItemService;
import com.example.evostyle.domain.payment.dto.event.PaymentEvent;
import com.example.evostyle.domain.product.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentKafkaListener {

    private final JsonHelper jsonHelper;
    private final ProductDetailService productDetailService;
    private final MemberService memberService;
    private final OrderItemService orderItemService;

    @KafkaListener(topics = "payment-event", groupId = "stock-handler")
    public void stockHandler(String payload) {
        PaymentEvent paymentEvent = jsonHelper.fromJson(payload, PaymentEvent.class);

        switch (paymentEvent.paymentEventType()){
            case CONFIRM -> productDetailService.decreaseStock(paymentEvent.orderItemIdList());
            case CANCEL -> productDetailService.increaseStock(paymentEvent.orderItemIdList());
        }
    }

    @KafkaListener(topics = "payment-event", groupId = "member-grade-handler")
    public void member(String payload) {
        PaymentEvent paymentEvent = jsonHelper.fromJson(payload, PaymentEvent.class);

        switch (paymentEvent.paymentEventType()){
            case CONFIRM -> memberService.increasePurchaseSum(paymentEvent.memberId(), paymentEvent.orderItemIdList());
            case CANCEL -> memberService.decreasePurchaseSum(paymentEvent.memberId(), paymentEvent.orderItemIdList());
        }
    }

    @KafkaListener(topics = "payment-event",  groupId = "order-status-handler")
    public void orderStatus(String payload) {
        PaymentEvent paymentEvent = jsonHelper.fromJson(payload, PaymentEvent.class);
        orderItemService.changeOrderItemStatus(paymentEvent.orderItemIdList(), OrderStatus.PAID);
    }
}
