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

    @PostMapping("brandes/{brandId}/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request,
                                                         @PathVariable (name = "brandId")Long brandId,
                                                         @PathVariable (name = "categoryId") Long categoryId){
        ProductResponse response = productService.createProduct(request, brandId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody UpdateProductRequest request,
                                                         @PathVariable (name = "productId") Long productId){

        ProductResponse response = productService.updateProduct(request, productId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "productId") Long productId){
        ProductResponse response = productService.findProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, Long>> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteProduct(productId);

        return  ResponseEntity.status(HttpStatus.OK).body(Map.of("productId", productId));
    }
}
