package com.example.evostyle.domain.payment.service;

import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCheckoutResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.payment.repository.PaymentRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InvalidException;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final WebClient webClient;

    final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    public PaymentCheckoutResponse checkoutPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));

        //if(order. 주문의 상태가 결제 완료이면){
        // 이미 결제된 주문이라는 예외를 발생시키기
        // }

        return PaymentCheckoutResponse.from(order);
    }

    @Transactional
    public PaymentResponse confirmPayment(PaymentConfirmRequest request, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalPriceSum() != request.amount()) {// 주문금액과 결제 금액이 일치하지 않는다면
            throw new InvalidException(ErrorCode.PAYMENT_INVALID_AMOUNT);
        }

        String encodedAuth = Base64.getEncoder().encodeToString(("test_sk_yL0qZ4G1VOlGMeM4w2xvroWb2MQY" + ":").getBytes());


        TossPaymentResponse tossResponse = webClient.post().uri(TOSS_CONFIRM_URL)
                .header("Authorization", "Basic " + encodedAuth)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("결제 승인 실패: " + errorBody))))
                .bodyToMono(TossPaymentResponse.class)
                .block();

        order.getOrderItemList().forEach(o -> o.updateOrderStatus(OrderStatus.PAID));// 주문 상태 변경
        //orderItem이 가지는 productDetailId와 일치하는 productDetail의 재고를 차감한다

        for (OrderItem orderItem : order.getOrderItemList()) {
            orderItem.getProductDetail().deductStock(orderItem.getEachAmount());
        }

        Member member = order.getMember();
        member.addToPurchaseSum(order.getTotalAmountSum());
        member.promoteGrade();

        Payment payment = Payment.of(order, tossResponse);

        paymentRepository.save(payment);
        return PaymentResponse.from(payment);
    }
}
