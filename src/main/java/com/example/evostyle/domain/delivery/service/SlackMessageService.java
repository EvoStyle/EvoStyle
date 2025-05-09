package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.AdminNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SlackMessageService {

    private final WebClient webClient = WebClient.create();
    private final SlackFormatter slackFormatter;

    public void sendMessage(AdminNotificationEvent adminNotificationEvent) {
        String payload = slackFormatter.formatAdminNotification(adminNotificationEvent);

        webClient.post()
                .uri(adminNotificationEvent.slackWebHookUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Slack 응답: " + response))
                .onErrorResume(e -> {
                    System.err.println("Slack 전송 실패: " + e.getMessage());
                    return Mono.empty();
                })
                .subscribe();
    }

}
