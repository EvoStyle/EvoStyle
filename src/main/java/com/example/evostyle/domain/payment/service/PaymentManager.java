package com.example.evostyle.domain.payment.service;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.payment.repository.PaymentRepository;
import com.example.evostyle.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentManager {

    private final WebClient webClient;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    @Value("${payment.toss.test-secret-key}")
    private String secretKey;

    final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";



    final String encodedAuth = Base64.getEncoder().encodeToString(("test_sk_yL0qZ4G1VOlGMeM4w2xvroWb2MQY" + ":").getBytes());
    private final PaymentRepository paymentRepository;


    public PaymentResponse confirmPayment(PaymentConfirmRequest request, Long orderId) {

        Order order = orderRepository.findOrderWithDetails(orderId);

        if (order == null){throw new NotFoundException(ErrorCode.ORDER_NOT_FOUND);}
        if (order.getTotalPriceSum() != request.amount()) {throw new InvalidException(ErrorCode.PAYMENT_INVALID_AMOUNT);}

        TossPaymentResponse tossResponse = webClient.post()
                .uri(TOSS_CONFIRM_URL)
                .header("Authorization", "Basic " + encodedAuth)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Toss API 에러 발생: {}", errorBody); //
                                    return Mono.error(new InternalServerException(ErrorCode.PAYMENT_SYSTEM_ERROR)); // 예외 던지기
                                })
                )
                .bodyToMono(TossPaymentResponse.class)
                .block();

        try {
            return paymentService.completeConfirmFlow(tossResponse, order.getId());
        } catch (Exception e) {
            revertPayment(PaymentCancelRequest.of("시스템 오류로 인해 결제가 실패했습니다"), tossResponse.paymentKey());
            throw new ConflictException(ErrorCode.PAYMENT_SYSTEM_ERROR);
        }
    }


    @Transactional(readOnly = true)
    public PaymentCancelResponse cancelPayment(PaymentCancelRequest request, String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_CANCEL_FAILED));

        boolean hasInvalidStatus = payment.getOrder().getOrderItemList().stream()
                .anyMatch(o -> o.getOrderStatus() != OrderStatus.PAID && o.getOrderStatus() != OrderStatus.PENDING);

        if (hasInvalidStatus) {
            throw new ConflictException(ErrorCode.PAYMENT_CANNOT_BE_CANCELED);
        }

        PaymentCancelResponse cancelResponse =  revertPayment(request, paymentKey);
        paymentService.completeCancelFlow(paymentKey);

        return cancelResponse;
    }


    private PaymentCancelResponse revertPayment(PaymentCancelRequest request, String paymentKey){
        PaymentCancelResponse cancelResponse = webClient.post()
                .uri("https://api.tosspayments.com/v1/payments/{paymentKey}/cancel", paymentKey)
                .header("Authorization", "Basic " + encodedAuth)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Toss 결제 취소 API 에러 발생: {}", errorBody);
                                    return Mono.error(new InternalServerException(ErrorCode.PAYMENT_CANCEL_FAILED));
                                })
                )
                .bodyToMono(PaymentCancelResponse.class)
                .block();

        return cancelResponse;
    }
}
