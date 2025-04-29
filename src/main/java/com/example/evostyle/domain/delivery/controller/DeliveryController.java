package com.example.evostyle.domain.delivery.controller;

import com.example.evostyle.domain.delivery.dto.request.DeliveryRequest;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/member/{memberId}")
    public ResponseEntity<DeliveryResponse> createDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @PathVariable Long memberId
    ) {
        DeliveryResponse deliveryResponse = deliveryService.createDelivery(deliveryRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryResponse);
    }
}
