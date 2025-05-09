package com.example.evostyle.domain.brand.controller;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateOwnerBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandNameRequest;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.dto.response.ReadBrandResponse;
import com.example.evostyle.domain.brand.dto.response.UpdateOwnerBrandCategoryResponse;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandNameResponse;
import com.example.evostyle.domain.brand.service.BrandService;
import com.example.evostyle.global.security.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final JwtUtil jwtUtil;

    @PostMapping("/brands")
    public ResponseEntity<CreateBrandResponse> createBrand(
            @RequestBody CreateBrandRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {


        CreateBrandResponse response = brandService.createBrand(request, authUser.memberId());

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

    @PatchMapping("/brands/{brandId}")
    public ResponseEntity<UpdateBrandNameResponse> updateBrand(
            @RequestBody @Valid UpdateBrandNameRequest request,
            @PathVariable(name = "brandId") Long brandId,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        UpdateBrandNameResponse response = brandService.updateBrand(request, brandId, authUser.memberId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/categories/brands/{brandId}")
    public ResponseEntity<UpdateOwnerBrandCategoryResponse> updateBrandCategories(
            @RequestBody @Valid UpdateOwnerBrandCategoryRequest request,
            @PathVariable(name = "brandId") Long brandId
    ) {
        UpdateOwnerBrandCategoryResponse response = brandService.updateBrandCategories(request, brandId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/brands/{brandId}")
    public ResponseEntity<Map<String, Long>> deleteBrand(
            @PathVariable(name = "brandId") Long brandId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = extractMemberId(httpServletRequest);

        brandService.deleteBrand(brandId, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("brandId", brandId));
    }

    private Long extractMemberId(HttpServletRequest httpServletRequest) {
        return jwtUtil.getMemberId(httpServletRequest.getHeader("Authorization"));
    }
}
