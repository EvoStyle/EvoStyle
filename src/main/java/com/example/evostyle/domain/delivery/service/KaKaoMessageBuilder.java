package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.UserNotificationEvent;
import org.springframework.stereotype.Service;

@Service
public class KaKaoMessageBuilder {


    public String buildTemplateObject(UserNotificationEvent userNotificationEvent) {
        String text = """
                %s
                송장번호: %s
                """.formatted(userNotificationEvent.message(), userNotificationEvent.trackingNumber());

        return """
            {
              "object_type": "text",
              "text": "%s",
              "link": {
                "web_url": "https://yourapp.com",
                "mobile_web_url": "https://yourapp.com"
              },
              "button_title": "앱으로 이동"
            }
            """.formatted(text.strip());
    }
}
