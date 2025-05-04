package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.DeliveryAdminEvent;
import com.example.evostyle.domain.delivery.dto.DeliveryEventWrapper;
import com.example.evostyle.domain.delivery.dto.DeliveryUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryKafkaListener {
    private final JsonHelper jsonHelper;
    private final DeliveryService deliveryService;

    @KafkaListener(topics = "delivery-event-topic", groupId = "delivery-update-group")
    public void updateDelivery(String message) {
        DeliveryEventWrapper deliveryEventWrapper = jsonHelper.fromJson(message, DeliveryEventWrapper.class);
        switch (deliveryEventWrapper.eventType()) {
            case USER_UPDATE -> {
                DeliveryUserEvent deliveryUserEvent = jsonHelper.convert(deliveryEventWrapper.payload(), DeliveryUserEvent.class);
                deliveryService.updateDelivery(deliveryUserEvent);
            }
            case ADMIN_UPDATE -> {
                DeliveryAdminEvent deliveryAdminEvent = jsonHelper.convert(deliveryEventWrapper.payload(), DeliveryAdminEvent.class);
                deliveryService.changeDeliveryStatusToShipped(deliveryAdminEvent);
            }
        }
    }
}
