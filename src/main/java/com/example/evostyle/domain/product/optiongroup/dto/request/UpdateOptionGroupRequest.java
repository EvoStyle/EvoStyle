package com.example.evostyle.domain.product.optiongroup.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateOptionGroupRequest(@NotBlank String name) {
}
