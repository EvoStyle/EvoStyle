package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateOptionRequest(@NotNull String type) {
}
