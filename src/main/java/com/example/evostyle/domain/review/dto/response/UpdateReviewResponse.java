package com.example.evostyle.domain.review.dto.response;

import com.example.evostyle.domain.review.entity.Review;

public record UpdateReviewResponse(
    Long id,
    String title,
    Byte rating,
    String contents
) {
    public static UpdateReviewResponse from(Review review) {
        return new UpdateReviewResponse(review.getId(), review.getTitle(), review.getRating(), review.getContents());
    }
}
