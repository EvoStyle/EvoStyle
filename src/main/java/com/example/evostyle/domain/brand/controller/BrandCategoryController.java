package com.example.evostyle.domain.brand.controller;

import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.service.BrandCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandCategoryController {

    private final BrandCategoryService brandCategoryService;

    @GetMapping("/brands/categories")
    public ResponseEntity<List<CategoryInfo>> readAllBrandCategories () {

        List<CategoryInfo> categoryInfoList = brandCategoryService.readAllBrandCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categoryInfoList);
    }

    @PatchMapping("/brands/{brandId}/categories")
    public ResponseEntity<UpdateBrandCategoryResponse> updateBrandCategories(
            @RequestBody @Valid @NotEmpty List<@Valid UpdateBrandCategoryRequest> requestList,
            @PathVariable(name = "brandId") Long brandId
    ) {
        UpdateBrandCategoryResponse response = brandCategoryService.updateBrandCategories(
                requestList,
                brandId
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}