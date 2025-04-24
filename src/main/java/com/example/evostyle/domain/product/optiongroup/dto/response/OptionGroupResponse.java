package com.example.evostyle.domain.product.optiongroup.dto.response;

import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;

import java.util.List;

public record OptionGroupResponse(
        Long id,
        Long productId,
        String name,
        List<OptionResponse>optionResponseList
) {

    public static OptionGroupResponse from(OptionGroup optionGroup, List<OptionResponse> optionResponseList){
        return new OptionGroupResponse(optionGroup.getId(), optionGroup.getProduct().getId(),
                                       optionGroup.getName(), optionResponseList);
    }
}
