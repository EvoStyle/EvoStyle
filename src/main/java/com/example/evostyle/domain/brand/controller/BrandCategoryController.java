package com.example.evostyle.domain.brand.controller;

import com.example.evostyle.domain.brand.dto.request.BrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.service.BrandCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandCategoryController {

    private final BrandCategoryService brandCategoryService;

    @PostMapping("/categories")
    public ResponseEntity<Map<String, String>> createBrandCategories(@RequestBody List<BrandCategoryRequest> brandCategoryRequest) {

        Map<String, String> brandCategoryList = brandCategoryService.createBrandCategories(brandCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(brandCategoryList);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryInfo>> readAllBrandCategories () {

        List<CategoryInfo> categoryInfoList = brandCategoryService.readAllBrandCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categoryInfoList);
    }

    @PatchMapping("/{brandId}/categories")
    public ResponseEntity<UpdateBrandCategoryResponse> updateBrandCategories(
            @RequestBody @Valid UpdateBrandCategoryRequest request,
            @PathVariable(name = "brandId") Long brandId
    ) {
        UpdateBrandCategoryResponse response = brandCategoryService.updateBrandCategories(
                request,
                brandId
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}