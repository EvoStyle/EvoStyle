package com.example.evostyle.domain.product.controller;

import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> readProduct(@PathVariable(name = "productId") Long productId) {
        ProductResponse response = productService.readProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestBody @Valid UpdateProductRequest request,
            @PathVariable(name = "productId") Long productId
    ) {
        ProductResponse response = productService.updateProduct(request, productId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, Long>> deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productId));
    }
}
