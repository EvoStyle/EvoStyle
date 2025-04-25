package com.example.evostyle.domain.product.optiongroup.dto.response;

import com.example.evostyle.domain.product.optiongroup.entity.Option;

public record OptionResponse(
        Long id,
        Long optionGroupId,
        String type
) {

    public static OptionResponse from(Option option){
        return new OptionResponse(option.getId(),
                                  option.getOptionGroup().getId(),
                                  option.getType());
    }
}
