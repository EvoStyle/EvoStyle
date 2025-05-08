package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateOwnerProductCategoryRequest(
        @NotNull
        @Size(max = 3)
        List<Long> categoryIdList
) {
}