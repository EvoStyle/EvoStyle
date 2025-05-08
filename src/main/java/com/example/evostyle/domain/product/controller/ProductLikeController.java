package com.example.evostyle.domain.product.controller;


import com.example.evostyle.domain.product.dto.response.ProductLikeResponse;
import com.example.evostyle.domain.product.service.ProductLikeService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PostMapping("products/{productId}/product-likes")
    public ResponseEntity<ProductLikeResponse> createProductLike(@PathVariable(name = "productId") Long productId,
                                                                 @AuthenticationPrincipal AuthUser authUser) {

        ProductLikeResponse productLikeResponse = productLikeService.createProductLike(authUser.memberId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(productLikeResponse);
    }


    @DeleteMapping("products/{productId}/product-likes")
    public ResponseEntity<Map<String, Long>> deleteProductLike(@PathVariable(name = "productId") Long productId,
                                                  @AuthenticationPrincipal AuthUser authUser) {

        productLikeService.deleteProductLike(authUser.memberId(), productId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productId", productId));

    }

    @GetMapping("/product-likes")
    public ResponseEntity<List<ProductLikeResponse>> readAllByMember(@AuthenticationPrincipal AuthUser authUser) {
        List<ProductLikeResponse> responseList = productLikeService.readAllByMember(authUser.memberId());
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
