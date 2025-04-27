package com.example.evostyle.domain.address.controller;

import com.example.evostyle.domain.address.dto.request.CreateAddressRequest;
import com.example.evostyle.domain.address.dto.request.UpdateAddressRequest;
import com.example.evostyle.domain.address.dto.response.CreateAddressResponse;
import com.example.evostyle.domain.address.dto.response.ReadAddressResponse;
import com.example.evostyle.domain.address.dto.response.UpdateAddressResponse;
import com.example.evostyle.domain.address.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/members/addresses")
    public ResponseEntity<List<ReadAddressResponse>> readAllAddresses(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");

        List<ReadAddressResponse> addressResponseList = addressService.readAllAddresses(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(addressResponseList);
    }

    @PatchMapping("members/addresses/{addressId}")
    public ResponseEntity<UpdateAddressResponse> updateAddress(
        @PathVariable(name = "addressId") Long addressId,
        @RequestBody UpdateAddressRequest request,
        HttpServletRequest servletRequest
    ) {
        Long memberId = (Long) servletRequest.getAttribute("memberId");

        UpdateAddressResponse updateAddressResponse = addressService.updateAddress(memberId, addressId,request);

        return ResponseEntity.status(HttpStatus.OK).body(updateAddressResponse);
    }
}
