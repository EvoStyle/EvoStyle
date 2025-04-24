package com.example.evostyle.domain.product.optiongroup.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateOptionRequest(@NotNull String type) {
}
