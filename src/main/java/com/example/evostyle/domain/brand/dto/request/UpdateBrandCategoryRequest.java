package com.example.evostyle.domain.brand.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateBrandCategoryRequest(
        @NotNull
        @Size(max = 3)
        List<Long> categoryIdList
) {
}