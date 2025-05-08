package com.example.evostyle.domain.product.controller;


import com.example.evostyle.domain.product.dto.request.UpdateProductDetailRequest;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.service.ProductDetailService;
import com.example.evostyle.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @PostMapping("/product-details")
    public ResponseEntity<List<ProductDetailResponse>> createProductDetail(@PathVariable(name = "productId")Long productId,
                                                                           @AuthenticationPrincipal AuthUser authUser){

        List<ProductDetailResponse> responseList = productDetailService.createProductDetail(productId, authUser.memberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    @GetMapping("/product-details")
    public ResponseEntity<List<ProductDetailResponse>> readByProductId(@PathVariable(name = "productId")Long productId){

        List<ProductDetailResponse> responseList = productDetailService.readByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/product-details/{productDetailId}")
    public ResponseEntity<ProductDetailResponse> readProductDetailById(@PathVariable(name = "productDetailId")Long productDetailId){

        ProductDetailResponse response = productDetailService.readProductDetailById(productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/product-details")
    public ResponseEntity<List<ProductDetailResponse>> updateProductDetailStock(@RequestBody List<@Valid UpdateProductDetailRequest> requestList,
                                                                                @RequestAttribute("memberId") Long memberId,
                                                                                @PathVariable(name = "productId")Long productId){

        List<ProductDetailResponse> responseList = productDetailService.updateProductDetailStock(requestList, productId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
