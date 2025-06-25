package com.example.evostyle.domain.payment.service;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderQueryDslImpl;
import com.example.evostyle.domain.payment.dto.event.PaymentEvent;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.payment.repository.PaymentRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InvalidException;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final OrderQueryDslImpl orderQueryDsl;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void findOrElseSavePayment(Order order, PaymentConfirmRequest request){

        paymentRepository.findByOrderId(order.getId())
                        .orElseGet(()->paymentRepository.save(Payment.of(order, request.paymentKey(), request.amount())));
    }

    @Transactional
    public void syncPaymentState(PaymentEvent paymentEvent){

        Payment payment = paymentRepository.findByPaymentKey(paymentEvent.paymentKey())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PAYMENT));

        payment.syncState(
                paymentEvent.paymentKey(),
                paymentEvent.paymentStatus(),
                paymentEvent.totalAmount(),
                paymentEvent.method(),
                paymentEvent.approvedAt(),
                paymentEvent.canceledAt()
        );
    }

    public Payment validatePaymentElseThrowException(String paymentKey) {

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_CANCEL_FAILED));

        boolean hasInvalidStatus = payment.getOrder().getOrderItemList().stream()
                .anyMatch(o -> o.getOrderStatus() != OrderStatus.PAID && o.getOrderStatus() != OrderStatus.PENDING);

        if (hasInvalidStatus) {throw new ConflictException(ErrorCode.PAYMENT_CANNOT_BE_CANCELED);}
        return payment;
    }

    public Order validateOrderElseThrowException(Long orderId, Integer amount) {

        Order order = orderQueryDsl.findByIdWithItemsAndProductDetail(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalPriceSum() != amount) {throw new InvalidException(ErrorCode.PAYMENT_INVALID_AMOUNT);}
        return order;
    }
}

