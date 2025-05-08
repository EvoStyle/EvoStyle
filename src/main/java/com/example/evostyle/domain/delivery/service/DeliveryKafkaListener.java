package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryKafkaListener {
    private final JsonHelper jsonHelper;
    private final DeliveryUpdateService deliveryUpdateService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KaKaoMessageService kaKaoMessageService;
    private final SlackMessageService slackMessageService;

    @KafkaListener(id = "listener-1",topics = "delivery-event-topic", groupId = "delivery-update-group")
    public void updateDelivery(String message) {
        DeliveryUserEvent deliveryEvent = jsonHelper.fromJson(message, DeliveryUserEvent.class);
        switch (deliveryEvent.eventType()) {
            case USER_UPDATE -> {
                Optional<Long> delivery = deliveryUpdateService.updateDelivery(deliveryEvent);
                if (delivery.isEmpty()) {
                    return;
                }
                UserNotificationEvent success = UserNotificationEvent.success(deliveryEvent.userId(), delivery.get());
                String payload = jsonHelper.toJson(success);
                kafkaTemplate.send("user-notification-topic", deliveryEvent.userId().toString(), payload);
            }
            case ADMIN_UPDATE -> {
                AdminDeliveryResponse adminDeliveryResponse = deliveryUpdateService.changeDeliveryStatusToShipped(deliveryEvent);
               AdminNotificationEvent success = AdminNotificationEvent.success(adminDeliveryResponse);
                String payload = jsonHelper.toJson(success);
                kafkaTemplate.send("admin-notification-topic", success.deliveryId().toString(), payload);
            }
        }
    }

    @KafkaListener(topics = "user-notification-topic", groupId = "user-notification-group")
    public void sendUser(String message) {
        UserNotificationEvent userNotificationEvent = jsonHelper.fromJson(message, UserNotificationEvent.class);
        kaKaoMessageService.sendMessage(userNotificationEvent);
    }

    @KafkaListener(topics = "admin-notification-topic", groupId = "admin-notification-group")
    public void sendAdmin(String message) {
        AdminNotificationEvent adminNotificationEvent = jsonHelper.fromJson(message, AdminNotificationEvent.class);
        slackMessageService.sendMessage(adminNotificationEvent);
    }
}
