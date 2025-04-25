package com.example.evostyle.domain.product.optiongroup.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateOptionRequest(@NotNull String type) {
}
