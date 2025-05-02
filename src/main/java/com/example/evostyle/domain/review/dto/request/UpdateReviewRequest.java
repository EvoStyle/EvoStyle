package com.example.evostyle.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateReviewRequest(
    String title,
    @Min(1)
    @Max(5)
    Byte rating,
    String contents
) {
}
