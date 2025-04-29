package com.example.evostyle.domain.product.optiongroup.dto.response;

public record OptionQueryDto(
        Long productDetailId,
        Long id,
        Long optionGroupId,
        String type

) {
}
