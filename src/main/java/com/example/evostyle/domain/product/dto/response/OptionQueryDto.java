package com.example.evostyle.domain.product.dto.response;

public record OptionQueryDto(
        Long productDetailId,
        Long id,
        Long optionGroupId,
        String type

) {
}
