package com.example.evostyle.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateReviewRequest(
    String title,

    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    Byte rating,

    String contents
) {
}
