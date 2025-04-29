package com.example.evostyle.domain.brand.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateBrandCategoryRequest(
        @NotNull
        List<Long> categoryIdList
) {
}