package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateProductDetailRequest(@NotNull Long productDetailId, @NotNull Integer stock) {
}
