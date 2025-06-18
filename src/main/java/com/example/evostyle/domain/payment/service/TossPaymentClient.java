package com.example.evostyle.domain.payment.service;

import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.global.config.PaymentProperties;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InternalServerException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentClient {

    private final WebClient webClient;
    private final PaymentProperties paymentProperties;

    private final String AUTH_HEADER =  "Authorization";

    public TossPaymentResponse sendConfirmRequest(PaymentConfirmRequest request) {
       return webClient.post()
                .uri(paymentProperties.getConfirmUri())
                .header(AUTH_HEADER, generateAuthHeader())
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
    }


    public PaymentCancelResponse sendCancelRequest(PaymentCancelRequest request, String paymentKey) {
      return webClient.post()
                .uri(paymentProperties.getCancelUri(), paymentKey)
                .header(AUTH_HEADER, generateAuthHeader())
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
    }

    private String generateAuthHeader(){
        return "Basic " + Base64.getEncoder().encodeToString((paymentProperties.getTestClientKey() + ":").getBytes());
    }
}
