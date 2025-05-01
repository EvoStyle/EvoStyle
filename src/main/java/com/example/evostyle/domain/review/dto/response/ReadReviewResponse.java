package com.example.evostyle.domain.review.dto.response;

import com.example.evostyle.domain.review.entity.Review;

public record ReadReviewResponse(
    Long id,
    String title,
    Byte rating
) {
    public static ReadReviewResponse from(Review review) {
        return new ReadReviewResponse(review.getId(), review.getTitle(), review.getRating());
    }
}
