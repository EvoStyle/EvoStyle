package com.example.evostyle.domain.payment.service;

import com.example.evostyle.domain.member.service.MemberService;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.service.OrderItemService;
import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.product.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentManager {

    private final PaymentService paymentService;
    private final TossPaymentClient tossPaymentClient;
    private final OrderItemService orderItemService;
    private final MemberService memberService;
    private final ProductDetailService productDetailService;

    public PaymentResponse handlePaymentConfirmationFlow(PaymentConfirmRequest request, Long orderId) {

        Order order = paymentService.validateOrderElseThrowException(orderId, request.amount());
        productDetailService.decreaseStock(order);

        try {
            TossPaymentResponse tossPaymentResponse = tossPaymentClient.sendConfirmRequest(request);
            orderItemService.changeOrderItemStatus(order.getOrderItemList(), OrderStatus.PAID);
            memberService.increasePurchaseSum(order.getMember().getId(), order.getTotalPriceSum());
            return paymentService.savePayment(order, tossPaymentResponse);

        } catch (Exception ex) {
            productDetailService.increaseStock(order);
            throw ex;
        }
    }

    public PaymentCancelResponse paymentCancel(PaymentCancelRequest request){

        Payment payment = paymentService.validatePaymentElseThrowException(request.paymentKey());
        PaymentCancelResponse cancelResponse = tossPaymentClient.sendCancelRequest(request);
        productDetailService.increaseStock(payment.getOrder());
        orderItemService.changeOrderItemStatus(payment.getOrder().getOrderItemList(), OrderStatus.PAID);
        memberService.decreasePurchaseSum(payment.getOrder().getMember().getId(), payment.getTotalAmount());
        return cancelResponse;
    }
}

