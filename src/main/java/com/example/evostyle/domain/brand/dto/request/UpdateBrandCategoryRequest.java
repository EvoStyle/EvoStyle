package com.example.evostyle.domain.brand.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateBrandCategoryRequest(
        @NotNull
        Long currentCategoryId,

        @NotNull
        Long newCategoryId
) {
}