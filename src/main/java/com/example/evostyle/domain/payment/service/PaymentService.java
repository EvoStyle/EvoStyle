package com.example.evostyle.domain.payment.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderQueryDslImpl;
import com.example.evostyle.domain.payment.dto.event.*;
import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.payment.repository.PaymentRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InvalidException;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final OrderQueryDslImpl orderQueryDsl;
    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonHelper jsonHelper;


    @Transactional
    public PaymentResponse confirmPayment(PaymentConfirmRequest request, Long orderId) {

        Order order = validateOrderElseThrowException(orderId, request.amount());
        TossPaymentResponse tossResponse = tossPaymentClient.sendConfirmRequest(request);

        paymentRepository.save(Payment.of(order, tossResponse));

        List<Long> orderItemIdList = order.getOrderItemList().stream().mapToLong(OrderItem::getId).boxed().toList();
        sendKafkaMessage(order.getMember().getId(), orderItemIdList, OrderStatus.PAID, PaymentEventType.CONFIRM);

        return PaymentResponse.from(tossResponse);
    }

    public PaymentCancelResponse cancelPayment(Long memberId, PaymentCancelRequest request) {
        Payment payment = validatePaymentElseThrowException(request.paymentKey());
        PaymentCancelResponse cancelResponse = tossPaymentClient.sendCancelRequest(request);

        List<Long> orderItemIdList = payment.getOrder().getOrderItemList().stream().mapToLong(OrderItem::getId).boxed().toList();
        sendKafkaMessage(memberId, orderItemIdList, OrderStatus.CANCELED, PaymentEventType.CANCEL);

        return cancelResponse;
    }

    private void sendKafkaMessage(Long memberId, List<Long> orderItemIdList, OrderStatus orderStatus,PaymentEventType paymentEventType) {

        PaymentEvent paymentEvent = PaymentEvent.of(memberId, orderItemIdList, paymentEventType);

        String paymentPayload = jsonHelper.toJson(paymentEvent);
        kafkaTemplate.send("payment-event", paymentPayload);
    }



    private Payment validatePaymentElseThrowException(String paymentKey) {// 이 메서드가 환불이 가능한 결제상태인지 확인하는 것
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_CANCEL_FAILED));

        boolean hasInvalidStatus = payment.getOrder().getOrderItemList().stream()
                .anyMatch(o -> o.getOrderStatus() != OrderStatus.PAID && o.getOrderStatus() != OrderStatus.PENDING);

        if (hasInvalidStatus) {throw new ConflictException(ErrorCode.PAYMENT_CANNOT_BE_CANCELED);}

        return payment;
    }

    private Order validateOrderElseThrowException(Long orderId, Integer amount) {
        Order order = orderQueryDsl.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalPriceSum() != amount) {
            throw new InvalidException(ErrorCode.PAYMENT_INVALID_AMOUNT);
        }
        return order;
    }
}
