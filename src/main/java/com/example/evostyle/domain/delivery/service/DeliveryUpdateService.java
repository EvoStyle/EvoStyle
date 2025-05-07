package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.AdminDeliveryResponse;
import com.example.evostyle.domain.delivery.dto.AdminNotificationEvent;
import com.example.evostyle.domain.delivery.dto.DeliveryUserEvent;
import com.example.evostyle.domain.delivery.dto.UserNotificationEvent;
import com.example.evostyle.domain.delivery.dto.response.ParcelResponse;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.DeliveryStatus;
import com.example.evostyle.domain.delivery.repository.DeliveryRepository;
import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.repository.AddressRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryUpdateService {
    private final JsonHelper jsonHelper;
    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ParcelApiService parcelApiService;
    private final DeliveryService deliveryService;
    private final AddressRepository addressRepository;

    public AdminDeliveryResponse changeDeliveryStatusToShipped(DeliveryUserEvent deliveryAdminEvent) {

        Delivery delivery = deliveryRepository.findWithOrderItemAndBrandAndMemberById(deliveryAdminEvent.deliveryId());
        if (delivery == null) {
            throw new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND);
        }
        if (delivery.getDeliveryStatus() != DeliveryStatus.READY) {
            if (delivery.getTrackingNumber().isEmpty()) {
                //log 찍고 알림을 보내는게 좋을듯 여기도달하면 단단히 잘못된것임
            }
            AdminNotificationEvent fail = AdminNotificationEvent.fail(delivery);
            String payload = jsonHelper.toJson(fail);
            kafkaTemplate.send("admin-notification-topic",delivery.getId().toString(),payload);
        }
        ParcelResponse parcelResponse = parcelApiService.createTrackingNumber(delivery);

        return deliveryService.performShipping(parcelResponse, delivery);

    }

    public String updateDelivery(DeliveryUserEvent deliveryUserEvent) {
        Delivery delivery = deliveryRepository.findById(deliveryUserEvent.deliveryId()).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        Address address = addressRepository.findById(deliveryUserEvent.addressId()).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.READY)) {
            if (parcelApiService.isCorrectionFailed(delivery, address, deliveryUserEvent)) {
                UserNotificationEvent fail = UserNotificationEvent.fail(deliveryUserEvent.userId(), delivery.getTrackingNumber());
                String payload = jsonHelper.toJson(fail);
                kafkaTemplate.send("user-notification-topic", deliveryUserEvent.userId().toString(), payload);
            }
        }
        Delivery savedDelivery = deliveryService.updateDelivery(deliveryUserEvent, delivery, address);
        return savedDelivery.getTrackingNumber();
    }
}
