package com.example.evostyle.domain.parcel.cotroller;

import com.example.evostyle.domain.parcel.exception.ParcelAlreadyReceivedException;
import com.example.evostyle.domain.parcel.service.ParcelService;
import com.example.evostyle.domain.parcel.dto.request.ParcelRequest;
import com.example.evostyle.domain.parcel.dto.response.ParcelResponse;
import com.example.evostyle.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/parcel")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    @PostMapping
    public ResponseEntity<?> createParcel(@RequestBody ParcelRequest parcelRequest) {
        try {
            ParcelResponse parcel = parcelService.createParcel(parcelRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(parcel);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteParcel(@PathVariable String parcelId) {
        try {
            parcelService.deleteParcel(parcelId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ParcelAlreadyReceivedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
