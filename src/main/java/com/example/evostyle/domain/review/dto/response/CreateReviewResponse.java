package com.example.evostyle.domain.review.dto.response;

import com.example.evostyle.domain.review.entity.Review;

public record CreateReviewResponse(Long id, String title) {
    public static CreateReviewResponse from(Review review) {
        return new CreateReviewResponse(review.getId(), review.getTitle());
    }
}
