package com.example.evostyle.domain.delivery.controller;

import com.example.evostyle.domain.delivery.dto.request.DeliveryRequest;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/address/{addressId}/member/{memberId}/order-items/{orderItemId}/delivery")
    public ResponseEntity<DeliveryResponse> createDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @PathVariable Long addressId,
            @PathVariable Long orderItemId,
            @PathVariable Long memberId
    ) {
        DeliveryResponse deliveryResponse = deliveryService.createDelivery(addressId, orderItemId,memberId,deliveryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryResponse);
    }


    @GetMapping("/members/{memberId}/delivery")
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveryByMember(@PathVariable Long memberId) {
        List<DeliveryResponse> deliveryResponses = deliveryService.getAllDeliveryByMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponses);
    }

    @PatchMapping("/address/{addressId}/order-items/{orderItemId}/delivery")
    public ResponseEntity<DeliveryResponse> updateDelivery(@RequestBody DeliveryRequest deliveryRequest) {
        DeliveryResponse deliveryResponse = deliveryService.updateDelivery(deliveryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponse);
    }

    @DeleteMapping("/delivery/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
