package com.example.evostyle.domain.product.controller;

import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> readProduct(@PathVariable(name = "productId") Long productId){
        ProductResponse response = productService.readProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody UpdateProductRequest request,
                                                         @PathVariable (name = "productId") Long productId){

        ProductResponse response = productService.updateProduct(request, productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, Long>> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteProduct(productId);

        return  ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productId));
    }
}
