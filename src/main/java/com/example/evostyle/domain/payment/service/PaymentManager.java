package com.example.evostyle.domain.payment.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.common.util.KafkaPublisher;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.payment.dto.event.PaymentEvent;
import com.example.evostyle.domain.payment.dto.event.PaymentStatus;
import com.example.evostyle.domain.payment.dto.event.StockRestoreEvent;
import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.product.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentManager {

    private final PaymentService paymentService;
    private final TossPaymentClient tossPaymentClient;
    private final ProductDetailService productDetailService;
    private final KafkaPublisher kafkaPublisher;
    private final JsonHelper jsonHelper;

    @Value("${spring.kafka.topic.payment-topic}")
    private String paymentTopic;

    @Value("${spring.kafka.topic.stock-restore-topic}")
    private String stockRestoreTopic;

    public PaymentResponse handlePaymentConfirmationFlow(PaymentConfirmRequest request, Long orderId) {

        Order order = paymentService.validateOrderElseThrowException(orderId, request.amount());
        productDetailService.decreaseStock(order.getId());
        paymentService.findOrElseSavePayment(order, request);

        try {
            TossPaymentResponse tossPaymentResponse = tossPaymentClient.sendConfirmRequest(request);
            PaymentEvent paymentEvent = PaymentEvent.of(orderId, OrderStatus.PAID, PaymentStatus.CONFIRMED, tossPaymentResponse);
            kafkaPublisher.sendWithCallBack(paymentTopic, orderId.toString(), jsonHelper.toJson(paymentEvent));
            return PaymentResponse.from(tossPaymentResponse);

        } catch (Exception ex) {
            StockRestoreEvent restoreEvent = StockRestoreEvent.from(orderId);
            kafkaPublisher.sendWithCallBack(stockRestoreTopic, orderId.toString(), jsonHelper.toJson(restoreEvent));
            throw ex;
        }
    }

    public PaymentCancelResponse handlePaymentCancelFlow(PaymentCancelRequest request) {

        Payment payment = paymentService.validatePaymentElseThrowException(request.paymentKey());
        TossPaymentResponse tossCancelResponse = tossPaymentClient.sendCancelRequest(request);
        PaymentEvent paymentCancelEvent = PaymentEvent.of(payment.getOrder().getId(), OrderStatus.CANCELED, PaymentStatus.CANCELED, tossCancelResponse);
        kafkaPublisher.sendWithCallBack(paymentTopic, payment.getOrder().getId().toString(), jsonHelper.toJson(paymentCancelEvent));
        return PaymentCancelResponse.from(tossCancelResponse);
    }
}

