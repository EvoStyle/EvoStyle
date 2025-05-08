package com.example.evostyle.domain.delivery.controller;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.DeliveryAdminUpdateEvent;
import com.example.evostyle.domain.delivery.dto.DeliveryUserUpdateEvent;
import com.example.evostyle.domain.delivery.dto.EventType;
import com.example.evostyle.domain.delivery.dto.request.DeliveryRequest;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponseForBrand;
import com.example.evostyle.domain.delivery.service.DeliveryService;
import com.example.evostyle.global.security.AuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonHelper jsonHelper;

    @PostMapping("/address/{addressId}/order-items/{orderItemId}/delivery")
    public ResponseEntity<DeliveryResponse> createDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @PathVariable Long addressId,
            @PathVariable Long orderItemId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        DeliveryResponse deliveryResponse = deliveryService.createDelivery(addressId, orderItemId,authUser.memberId(),deliveryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryResponse);
    }


    @GetMapping("/delivery")
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveryByMember(@AuthenticationPrincipal AuthUser authUser) {
        List<DeliveryResponse> deliveryResponseList = deliveryService.getAllDeliveryByMember(authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseList);
    }

    @GetMapping("/brands/{brandId}/delivery")
    public ResponseEntity<List<DeliveryResponseForBrand>> getAllDeliveryByBrand(@PathVariable Long brandId) {
        List<DeliveryResponseForBrand> deliveryResponseList = deliveryService.getAllDeliveryByBrand(brandId);
        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseList);
    }

    @PatchMapping("/address/{addressId}/delivery/{deliveryId}")
    public ResponseEntity<DeliveryResponse> updateDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @PathVariable Long addressId,
            @PathVariable Long deliveryId,
            @AuthenticationPrincipal AuthUser authUser

    ) {
        DeliveryUserUpdateEvent deliveryUserUpdateEvent = DeliveryUserUpdateEvent.of(EventType.USER_UPDATE, authUser.memberId(),deliveryId, addressId, deliveryRequest.deliveryRequest());
        String payload = jsonHelper.toJson(deliveryUserUpdateEvent);
        kafkaTemplate.send("delivery-event-topic", deliveryId.toString(), payload);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // 권한설정필요 관리자용
    @PatchMapping("/delivery/{deliveryId}")
    public ResponseEntity<DeliveryResponse> changeDeliveryStatusToShipped(@PathVariable Long deliveryId) {
        DeliveryAdminUpdateEvent deliveryAdminUpdateEvent = DeliveryAdminUpdateEvent.of(EventType.ADMIN_UPDATE, deliveryId);
        String payload = jsonHelper.toJson(deliveryAdminUpdateEvent);
        kafkaTemplate.send("delivery-event-topic", deliveryId.toString(), payload);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
