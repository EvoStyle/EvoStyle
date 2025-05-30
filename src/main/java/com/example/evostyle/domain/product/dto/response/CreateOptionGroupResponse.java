package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.OptionGroup;

import java.util.List;

public record CreateOptionGroupResponse(
        Long id,
        Long productId,
        String name,
        List<OptionResponse>optionResponseList
) {

    public static CreateOptionGroupResponse from(OptionGroup optionGroup, List<OptionResponse> optionResponseList){
        return new CreateOptionGroupResponse(optionGroup.getId(), optionGroup.getProduct().getId(),
                                       optionGroup.getName(), optionResponseList);
    }
}
