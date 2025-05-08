package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateProductRequest(
        @NotNull
        Long brandId,

        @NotBlank
        @Size(max = 10)
        String name,

        @NotBlank
        String description,

        @NotNull
        Integer price,

        @NotNull
        @Size(max = 3)
        List<Long> categoryIdList
) {
}
