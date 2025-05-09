package com.example.evostyle.domain.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateBrandRequest(
        @NotBlank
        @Size(max = 10)
        String name,

        @NotNull
        @Size(max = 3)
        List<Long> categoryIdList,

        String slackWebHookUrl
) {
}