package com.example.evostyle.domain.product.controller;

import com.example.evostyle.domain.product.dto.request.CreateProductCategoryRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductCategoryRequest;
import com.example.evostyle.domain.product.dto.response.ReadProductCategoryResponse;
import com.example.evostyle.domain.product.dto.response.UpdateProductCategoryResponse;
import com.example.evostyle.domain.product.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createProductCategories(@RequestBody List<CreateProductCategoryRequest> requestList) {

        Map<String, String> response = productCategoryService.createProductCategories(requestList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReadProductCategoryResponse>> readAllProductCategories() {

        List<ReadProductCategoryResponse> responseList = productCategoryService.readAllProductCategories();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("{productCategoryId}")
    public ResponseEntity<ReadProductCategoryResponse> readProductCategoryById(@PathVariable(name = "productCategoryId") Long productCategoryId) {

        ReadProductCategoryResponse response = productCategoryService.readProductCategoryById(productCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("{productCategoryId}")
    public ResponseEntity<UpdateProductCategoryResponse> updateProductCategory(
            @RequestBody UpdateProductCategoryRequest request,
            @PathVariable(name = "productCategoryId") Long productCategoryId
    ) {
        UpdateProductCategoryResponse response = productCategoryService.updateProductCategory(request, productCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{productCategoryId}")
    public ResponseEntity<Map<String, Long>> deleteProductCategory(@PathVariable(name = "productCategoryId") Long productCategoryId) {

        productCategoryService.deleteProductCategory(productCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productCategoryId", productCategoryId));
    }
}
