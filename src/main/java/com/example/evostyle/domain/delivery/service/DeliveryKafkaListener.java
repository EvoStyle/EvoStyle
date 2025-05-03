package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.DeliveryAdminEvent;
import com.example.evostyle.domain.delivery.dto.DeliveryEventWrapper;
import com.example.evostyle.domain.delivery.dto.DeliveryUserEvent;
import com.example.evostyle.domain.delivery.dto.UserNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryKafkaListener {
    private final JsonHelper jsonHelper;
    private final DeliveryService deliveryService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KaKaoMessageService kaKaoMessageService;

    @KafkaListener(topics = "delivery-event-topic", groupId = "delivery-update-group")
    public void updateDelivery(String message) {
        DeliveryEventWrapper deliveryEventWrapper = jsonHelper.fromJson(message, DeliveryEventWrapper.class);
        switch (deliveryEventWrapper.eventType()) {
            case USER_UPDATE -> {
                DeliveryUserEvent deliveryUserEvent = jsonHelper.convert(deliveryEventWrapper.payload(), DeliveryUserEvent.class);
                String trackingNumber = deliveryService.updateDelivery(deliveryUserEvent);
                UserNotificationEvent success = UserNotificationEvent.success(deliveryUserEvent.userId(), trackingNumber);
                String payload = jsonHelper.toJson(success);
                kafkaTemplate.send("user-notification-topic", deliveryUserEvent.userId().toString(), payload);
            }
            case ADMIN_UPDATE -> {
                DeliveryAdminEvent deliveryAdminEvent = jsonHelper.convert(deliveryEventWrapper.payload(), DeliveryAdminEvent.class);
                deliveryService.changeDeliveryStatusToShipped(deliveryAdminEvent);
          //      kafkaTemplate.send("admin-notification-topic",,)
            }
        }
    }

    @KafkaListener(topics = "user-notification-topic", groupId = "user-notification-group")
    public void sendUser(String message) {
        UserNotificationEvent userNotificationEvent = jsonHelper.fromJson(message, UserNotificationEvent.class);
        kaKaoMessageService.sendMessage(userNotificationEvent);
    }
}
