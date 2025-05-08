package com.example.evostyle.domain.brand.controller;

import com.example.evostyle.domain.brand.dto.request.CreateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.ReadProductCategoryResponse;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.service.BrandCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brand-categories")
@RequiredArgsConstructor
public class BrandCategoryController {

    private final BrandCategoryService brandCategoryService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createBrandCategories(@RequestBody List<CreateBrandCategoryRequest> requestList) {

        Map<String, String> response = brandCategoryService.createBrandCategories(requestList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReadProductCategoryResponse>> readAllBrandCategories() {

        List<ReadProductCategoryResponse> responseList = brandCategoryService.readAllBrandCategories();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("{brandCategoryId}")
    public ResponseEntity<ReadProductCategoryResponse> readBrandCategoryById(@PathVariable(name = "brandCategoryId") Long brandCategoryId) {

        ReadProductCategoryResponse response = brandCategoryService.readBrandCategoryById(brandCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("{brandCategoryId}")
    public ResponseEntity<UpdateBrandCategoryResponse> updateBrandCategory(
            @RequestBody UpdateBrandCategoryRequest request,
            @PathVariable(name = "brandCategoryId") Long brandCategoryId
    ) {
        UpdateBrandCategoryResponse response = brandCategoryService.updateBrandCategory(request, brandCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{brandCategoryId}")
    public ResponseEntity<Map<String, Long>> deleteBrandCategory(@PathVariable(name = "brandCategoryId") Long brandCategoryId) {

        brandCategoryService.deleteBrandCategory(brandCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("brandCategoryId", brandCategoryId));
    }
}