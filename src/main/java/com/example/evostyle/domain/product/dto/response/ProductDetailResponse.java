package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;

import java.util.List;

public record ProductDetailResponse(
        Long id,
        Long productId,
        List<OptionResponse> optionResponseList,
        Integer stock
) {
    public static ProductDetailResponse from(ProductDetail productDetail,  List<OptionResponse> optionResponseList){
        return new ProductDetailResponse(productDetail.getId(),
                                        productDetail.getProduct().getId(),
                                        optionResponseList,
                                        productDetail.getStock());
    }

}
