package com.example.evostyle.domain.parcel.cotroller;

import com.example.evostyle.domain.parcel.service.ParcelService;
import com.example.evostyle.domain.parcel.dto.request.ParcelRequest;
import com.example.evostyle.domain.parcel.dto.response.ParcelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    @PostMapping
    public ResponseEntity<ParcelResponse> createParcel(@RequestBody ParcelRequest parcelRequest) {
        ParcelResponse parcel = parcelService.createParcel(parcelRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(parcel);
    }
}
