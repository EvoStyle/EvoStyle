package com.example.evostyle.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductDetailRequest(
        @NotNull Integer stock ,
        @NotNull List<Long> optionListId
) {
}
