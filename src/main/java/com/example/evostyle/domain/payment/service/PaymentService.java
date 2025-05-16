package com.example.evostyle.domain.payment.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.payment.dto.event.PaymentCanceledEvent;
import com.example.evostyle.domain.payment.dto.event.PaymentConfirmEvent;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient webClient;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonHelper jsonHelper;

    @Value("${toss.test.secret.key}")
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

        String payload = jsonHelper.toJson(PaymentConfirmEvent.from(order, tossResponse));
        kafkaTemplate.send("payment-completed", payload);

        paymentRepository.save(Payment.of(order, tossResponse));

        return PaymentResponse.from(tossResponse);
    }


    public PaymentCancelResponse cancelPayment(PaymentCancelRequest request, String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_CANCEL_FAILED));

        boolean hasInvalidStatus = payment.getOrder().getOrderItemList().stream()
                .anyMatch(o -> o.getOrderStatus() != OrderStatus.PAID && o.getOrderStatus() != OrderStatus.PENDING);

        if (hasInvalidStatus) {throw new ConflictException(ErrorCode.PAYMENT_CANNOT_BE_CANCELED);}

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

        String payload = jsonHelper.toJson(PaymentCanceledEvent.of(payment.getOrder().getMember().getId(), payment.getOrder().getId(), paymentKey));
        kafkaTemplate.send("payment-canceled", payload);

        return cancelResponse;
    }
}
