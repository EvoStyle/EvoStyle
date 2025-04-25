package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.product.dto.request.CreateProductDetailRequest;
import com.example.evostyle.domain.product.dto.request.ProductDetailResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;

    public ProductDetailResponse createProductDetail(CreateProductDetailRequest request, Long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductDetail productDetail = ProductDetail.of(product, request.stock());
        productDetailRepository.save(productDetail);

        return ProductDetailResponse.from(productDetail);
    }
}
