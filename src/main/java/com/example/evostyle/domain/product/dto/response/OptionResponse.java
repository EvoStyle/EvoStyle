package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.Option;

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

    public static OptionResponse from(OptionQueryDto response){
        return new OptionResponse(
                                  response.id(),
                                  response.optionGroupId(),
                                  response.type()

        );
    }
}
