package com.example.evostyle.domain.product.controller;

import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request,
                                                         HttpServletRequest servletRequest){

        Long memberId = (Long) servletRequest.getAttribute("memberId");
        ProductResponse response = productService.createProduct(request, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> readProduct(@PathVariable(name = "productId") Long productId){
        ProductResponse response = productService.readProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody UpdateProductRequest request,
                                                         @PathVariable (name = "productId") Long productId,
                                                         HttpServletRequest servletRequest){

        Long memberId = (Long) servletRequest.getAttribute("memberId");
        ProductResponse response = productService.updateProduct(request, productId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, Long>> deleteProduct(@PathVariable(name = "productId") Long productId,
                                                           HttpServletRequest servletRequest){

        Long memberId = (Long) servletRequest.getAttribute("memberId");
        productService.deleteProduct(productId,  memberId);
        return  ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productId));
    }
}
