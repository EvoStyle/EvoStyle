package com.example.evostyle.domain.product.controller;

import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.service.ProductService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request,
                                                         @AuthenticationPrincipal AuthUser authUser){


        ProductResponse response = productService.createProduct(authUser.memberId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> readProduct(@PathVariable(name = "productId") Long productId){
        ProductResponse response = productService.readProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/brands/{brandId}/products")
    public ResponseEntity<List<ProductResponse>> readByBrand(@PathVariable(name = "brandId")Long brandId,
                                                             @AuthenticationPrincipal AuthUser authUser){
        List<ProductResponse> response = productService.readByBrand(authUser.memberId(),brandId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@AuthenticationPrincipal AuthUser authUser,
                                                         @RequestBody UpdateProductRequest request,
                                                         @PathVariable (name = "productId") Long productId){

        ProductResponse response = productService.updateProduct(authUser.memberId(), request, productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, Long>> deleteProduct(@PathVariable(name = "productId") Long productId,
                                                           @AuthenticationPrincipal AuthUser authUser){
        productService.deleteProduct(authUser.memberId(), productId);

        return  ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productId));
    }
}
