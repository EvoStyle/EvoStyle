package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.UserNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        String encoded = URLEncoder.encode(templateJson, StandardCharsets.UTF_8);

        webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue("template_object=" + encoded)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("카카오 응답: " + response))
                .subscribe();
    }

}
