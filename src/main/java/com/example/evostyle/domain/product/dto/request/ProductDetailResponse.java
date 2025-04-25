package com.example.evostyle.domain.product.dto.request;

import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetailOption;

import java.util.List;

public record ProductDetailResponse(
        Long id,
        Long productId,
        List<ProductDetailOption> productDetailOptionList,
        Integer stock
) {
    public static ProductDetailResponse from(ProductDetail productDetail, List<ProductDetailOption> productDetailOptionList){
        return new ProductDetailResponse(productDetail.getId(),
                                        productDetail.getProduct().getId(),
                                        productDetailOptionList,
                                        productDetail.getStock());
    }

}
