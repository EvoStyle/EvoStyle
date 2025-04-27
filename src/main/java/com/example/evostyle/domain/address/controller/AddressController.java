package com.example.evostyle.domain.address.controller;

import com.example.evostyle.domain.address.dto.request.CreateAddressRequest;
import com.example.evostyle.domain.address.dto.response.CreateAddressResponse;
import com.example.evostyle.domain.address.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/members/addresses")
    public ResponseEntity<CreateAddressResponse> createAddress(
        @RequestBody CreateAddressRequest request,
        HttpServletRequest servletRequest
    ) {
        Long memberId = (Long) servletRequest.getAttribute("memberId");

        CreateAddressResponse addressResponse = addressService.createAddress(memberId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponse);
    }
}
