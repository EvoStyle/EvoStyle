package com.example.evostyle.domain.product.controller;


import com.example.evostyle.domain.product.dto.request.UpdateProductDetailRequest;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.service.ProductDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @PostMapping("/products/{productId}/productDetails")
    public ResponseEntity<List<ProductDetailResponse>> createProductDetail(@PathVariable(name = "productId")Long productId){

        List<ProductDetailResponse> responseList = productDetailService.createProductDetail(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    @GetMapping("/products/{productId}/productDetails")
    public ResponseEntity<List<ProductDetailResponse>> readByProduct(@PathVariable(name = "productId")Long productId){

        List<ProductDetailResponse> responseList = productDetailService.readByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/productDetails/{productDetailId}")
    public ResponseEntity<ProductDetailResponse> readProductDetail(@PathVariable(name = "productDetailId")Long productDetailId){

        ProductDetailResponse response = productDetailService.readProductDetail(productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/products/{productId}/productDetails")
    public ResponseEntity<List<ProductDetailResponse>> updateProductDetailStock(@RequestBody List<@Valid UpdateProductDetailRequest> requestList,
                                                                    @PathVariable(name = "productId")Long productId){

        List<ProductDetailResponse> responseList = productDetailService.updateProductDetailStock(requestList, productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
