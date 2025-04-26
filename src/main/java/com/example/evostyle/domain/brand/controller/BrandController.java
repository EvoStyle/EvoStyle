package com.example.evostyle.domain.brand.controller;

import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandNameRequest;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.dto.response.ReadBrandResponse;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandNameResponse;
import com.example.evostyle.domain.brand.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brands")
    public ResponseEntity<CreateBrandResponse> createBrand(@RequestBody CreateBrandRequest request) {
        CreateBrandResponse response = brandService.createBrand(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<ReadBrandResponse>> readAllBrands() {
        List<ReadBrandResponse> responseList = brandService.readAllBrands();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<ReadBrandResponse> readBrandById(@PathVariable(name = "brandId") Long brandId) {

        ReadBrandResponse response = brandService.readBrandById(brandId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/brands/{brandId}")
    public ResponseEntity<UpdateBrandNameResponse> updateBrandName(
            @RequestBody @Valid UpdateBrandNameRequest request,
            @PathVariable(name = "brandId") Long brandId
    ) {
        UpdateBrandNameResponse response = brandService.updateBrandName(request, brandId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
