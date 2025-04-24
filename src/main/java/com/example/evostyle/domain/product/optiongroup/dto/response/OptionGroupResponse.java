package com.example.evostyle.domain.product.optiongroup.dto.response;

import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;

public record OptionGroupResponse(Long id, String name) {

    public static OptionGroupResponse from(OptionGroup optionGroup){
        return new OptionGroupResponse(optionGroup.getId(),
                                      optionGroup.getName());
    }
}
