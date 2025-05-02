package com.example.evostyle.domain.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBrandNameRequest(
        @NotBlank
        @Size(min = 2, max = 10)
        String name
) {
}