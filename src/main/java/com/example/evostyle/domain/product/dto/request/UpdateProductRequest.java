package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @Size(max = 10)
        String name,

        String description,

        Integer price
) {
}
