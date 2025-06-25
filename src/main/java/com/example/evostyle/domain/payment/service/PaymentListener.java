package com.example.evostyle.domain.payment.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.common.util.KafkaPublisher;
import com.example.evostyle.domain.member.service.MemberService;
import com.example.evostyle.domain.order.service.OrderItemService;
import com.example.evostyle.domain.payment.dto.event.PaymentEvent;
import com.example.evostyle.domain.payment.dto.event.StockRestoreEvent;
import com.example.evostyle.domain.product.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final JsonHelper jsonHelper;
    private final ProductDetailService productDetailService;
    private final PaymentService paymentService;
    private final MemberService memberService;
    private final OrderItemService orderItemService;
    private final KafkaPublisher kafkaPublisher;

    @KafkaListener(topics = "${spring.kafka.topic.payment-topic}", groupId = "member-grade-handler")
    public void handelMemberGrade(String payload) {
        PaymentEvent paymentEvent = jsonHelper.fromJson(payload, PaymentEvent.class);
        switch (paymentEvent.paymentStatus()) {
            case CONFIRMED -> memberService.increasePurchaseSum(paymentEvent.orderId());
            case CANCELED -> {
                memberService.decreasePurchaseSum(paymentEvent.orderId());

                StockRestoreEvent restoreEvent = StockRestoreEvent.from(paymentEvent.orderId());
                kafkaPublisher.sendWithCallBack("${spring.kafka.topic.stock-restore-topic}"
                        , paymentEvent.orderId().toString(), jsonHelper.toJson(restoreEvent));
            }
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.payment-topic}", groupId = "orderStatus-handler")
    public void handelOrderStatus(String payload) {
        PaymentEvent paymentEvent = jsonHelper.fromJson(payload, PaymentEvent.class);
        orderItemService.changeOrderItemStatus(paymentEvent.orderId(), paymentEvent.status());
    }

    @KafkaListener(topics = "${spring.kafka.topic.payment-topic}", groupId = "payment-sync-handler")
    public void handlePaymentSync(String payload) {
        PaymentEvent paymentEvent = jsonHelper.fromJson(payload, PaymentEvent.class);
        paymentService.syncPaymentState(paymentEvent);
    }

    @KafkaListener(topics = "${spring.kafka.topic.stock-restore-topic}", groupId = "stock-restore-handler")
    public void handelStockRestore(String payload) {
        StockRestoreEvent restoreEvent = jsonHelper.fromJson(payload, StockRestoreEvent.class);
        productDetailService.increaseStock(restoreEvent.orderId());
    }
}
