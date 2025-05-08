package com.example.evostyle.domain.member.controller;

import com.example.evostyle.domain.member.dto.request.CreateAddressRequest;
import com.example.evostyle.domain.member.dto.request.UpdateAddressRequest;
import com.example.evostyle.domain.member.dto.response.CreateAddressResponse;
import com.example.evostyle.domain.member.dto.response.ReadAddressResponse;
import com.example.evostyle.domain.member.dto.response.UpdateAddressResponse;
import com.example.evostyle.domain.member.service.AddressService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/members/addresses")
@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<CreateAddressResponse> createAddress(
        @RequestBody CreateAddressRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        CreateAddressResponse addressResponse = addressService.createAddress(memberId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReadAddressResponse>> readAllAddresses(
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        List<ReadAddressResponse> addressResponseList = addressService.readAllAddresses(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(addressResponseList);
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<UpdateAddressResponse> updateAddress(
        @PathVariable(name = "addressId") Long addressId,
        @RequestBody UpdateAddressRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        UpdateAddressResponse updateAddressResponse = addressService.updateAddress(memberId, addressId, request);

        return ResponseEntity.status(HttpStatus.OK).body(updateAddressResponse);
    }

    @PatchMapping("/{addressId}/isBasecamp")
    public ResponseEntity<UpdateAddressResponse> updateIsBasecamp(
        @PathVariable(name = "addressId") Long addressId,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        UpdateAddressResponse updateAddressResponse = addressService.updateIsBasecamp(memberId, addressId);

        return ResponseEntity.status(HttpStatus.OK).body(updateAddressResponse);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Map<String, Long>> deleteAddress(
        @PathVariable(name = "addressId") Long addressId,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        addressService.deleteAddress(addressId, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("addressId", addressId));
    }
}
