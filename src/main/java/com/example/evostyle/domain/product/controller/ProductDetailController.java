package com.example.evostyle.domain.product.controller;


import com.example.evostyle.domain.product.dto.request.CreateProductDetailRequest;

import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.service.ProductDetailService;
import com.example.evostyle.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @PostMapping("/products/{productId}/productDetails")
    public ResponseEntity<ProductDetailResponse> createProductDetail(@RequestBody CreateProductDetailRequest request,
                                                                     @PathVariable(name = "productId")Long productId){

        ProductDetailResponse response = productDetailService.createProductDetail(request, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/productDetails/{productDetailId}")
    public ResponseEntity<ProductDetailResponse> readProductDetail(@PathVariable(name = "productDetailId")Long productDetailId){

        ProductDetailResponse response = productDetailService.readProductDetail(productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/productDetails/{productDetailId}")
    public ResponseEntity<Map<String, Long>> deleteProductDetail(@PathVariable(name = "productDetailId")Long productDetailId){

        productDetailService.deleteProductDetail(productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productDetailId));
    }

}
