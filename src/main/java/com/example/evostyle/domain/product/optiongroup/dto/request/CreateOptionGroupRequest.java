package com.example.evostyle.domain.product.optiongroup.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOptionGroupRequest(
        @NotBlank
        @Size(min = 1 , max = 10)
        String name,
        @NotNull
        List<CreateOptionRequest> optionRequestList
){
}
