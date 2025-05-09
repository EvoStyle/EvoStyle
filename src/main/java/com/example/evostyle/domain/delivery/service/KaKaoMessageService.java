package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.UserNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KaKaoMessageService {
    @Value("${kakao.token}")
    private String token;

    @Value("${kakao.url}")
    private String url;
    private final KaKaoMessageBuilder kaKaoMessageBuilder;
    private final WebClient webClient = WebClient.create();

    public void sendMessage(UserNotificationEvent userNotificationEvent) {
        String templateJson = kaKaoMessageBuilder.buildTemplateObject(userNotificationEvent);

        webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(BodyInserters.fromFormData("template_object", templateJson))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("카카오 응답: " + response))
                .subscribe(
                        response -> log.info("✅ 카카오 응답 성공: {}", response),
                        error -> {
                            if (error instanceof WebClientResponseException e) {
                                log.warn("❌ 카카오 응답 실패: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                            } else {
                                log.error("❌ 카카오 메시지 전송 중 알 수 없는 오류 발생", error);
                            }
                        }
                );
    }

}
