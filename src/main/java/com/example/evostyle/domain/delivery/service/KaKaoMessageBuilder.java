package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.UserNotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

@Service
public class KaKaoMessageBuilder {


    public String buildTemplateObject(UserNotificationEvent userNotificationEvent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String text;
            if (userNotificationEvent.trackingNumber() != null && !userNotificationEvent.trackingNumber().isBlank()) {
                text = "%s\n송장번호: %s".formatted(userNotificationEvent.message(), userNotificationEvent.trackingNumber());
            } else {
                text = "%s\n배송 ID: %d".formatted(userNotificationEvent.message(), userNotificationEvent.deliveryId());
            }

            ObjectNode root = objectMapper.createObjectNode();
            root.put("object_type", "text");
            root.put("text", text);

            ObjectNode link = objectMapper.createObjectNode();
            link.put("web_url", "https://developers.kakao.com");
            link.put("mobile_web_url", "https://developers.kakao.com");

            root.set("link", link);
            root.put("button_title", "앱으로 이동");

            return objectMapper.writeValueAsString(root);

        } catch (Exception e) {
            throw new RuntimeException("카카오 성공 템플릿 JSON 생성 실패", e);
        }
    }
}
