package com.example.evostyle.domain.product.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;


public record CreateProductRequest(
        @NotBlank
        @Size(max = 10)
        String name,
        @NotBlank
        String description,
        @NotNull
        Integer price
) {
}
