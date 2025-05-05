package com.example.evostyle.domain.product.controller;


import com.example.evostyle.domain.product.dto.response.ProductLikeResponse;
import com.example.evostyle.domain.product.service.ProductLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                                                                 @RequestAttribute("memberId") Long memberId) {

        ProductLikeResponse productLikeResponse = productLikeService.createProductLike(memberId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(productLikeResponse);
    }


    @DeleteMapping("products/{productId}/product-likes")
    public ResponseEntity<Void> deleteProductLike(@PathVariable(name = "productId") Long productId,
                                                  @RequestAttribute("memberId") Long memberId) {

        productLikeService.deleteProductLike(memberId, productId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/product-likes")
    public ResponseEntity<List<ProductLikeResponse>> readAllByMember(@RequestAttribute("memberId") Long memberId) {
        List<ProductLikeResponse> responseList = productLikeService.readAllByMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
