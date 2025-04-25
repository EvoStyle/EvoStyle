package com.example.evostyle.domain.brand.controller;

import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.service.BrandService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brands")
    public ResponseEntity<CreateBrandResponse> createBrand(
            @RequestBody CreateBrandRequest request,
            HttpServletRequest httpServletRequest
    ) {
        CreateBrandResponse response = brandService.createBrand(request, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
