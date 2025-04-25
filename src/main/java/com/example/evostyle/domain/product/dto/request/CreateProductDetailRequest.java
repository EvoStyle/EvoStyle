package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateProductDetailRequest(@NotNull Integer stock ) {
}
