package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.AdminNotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SlackFormatter {

    private final JsonHelper jsonHelper;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String formatAdminNotification(AdminNotificationEvent event) {
        Map<String, Object> payload = new LinkedHashMap<>();

        List<Object> blocks = new ArrayList<>();

        blocks.add(Map.of(
                "type", "header",
                "text", Map.of(
                        "type", "plain_text",
                        "text", "📦 배송 상태 알림",
                        "emoji", true
                )
        ));

        blocks.add(Map.of(
                "type", "section",
                "fields", List.of(
                        markdownField("*배송 ID:*", event.deliveryId().toString()),
                        markdownField("*트래킹 번호:*", event.trackingNumber()),
                        markdownField("*요청자:*", event.memberNickname()),
                        markdownField("*연락처:*", event.phoneNumber())
                )
        ));

        blocks.add(Map.of(
                "type", "section",
                "text", Map.of(
                        "type", "mrkdwn",
                        "text", "*주소:*\n" + event.address() + " (" + event.addressAssistant() + ")"
                )
        ));

        blocks.add(Map.of(
                "type", "context",
                "elements", List.of(
                        Map.of("type", "mrkdwn", "text", "*요청사항:* " + event.deliveryRequest()),
                        Map.of("type", "mrkdwn", "text", "*성공 여부:* " + (event.success() ? "✅" : "❌"))
                )
        ));

        blocks.add(Map.of(
                "type", "context",
                "elements", List.of(
                        Map.of("type", "mrkdwn", "text", "*메시지:* " + event.message())
                )
        ));

        blocks.add(Map.of(
                "type", "context",
                "elements", List.of(
                        Map.of("type", "mrkdwn", "text", "🕑 *생성 시각:* " + event.createAt().format(formatter)),
                        Map.of("type", "mrkdwn", "text", "✏️ *수정 시각:* " + event.updateAt().format(formatter))
                )
        ));

        payload.put("blocks", blocks);

            return jsonHelper.toJson(payload);
    }

    private Map<String, Object> markdownField(String title, String value) {
        return Map.of("type", "mrkdwn", "text", title + "\n" + value);
    }
}
