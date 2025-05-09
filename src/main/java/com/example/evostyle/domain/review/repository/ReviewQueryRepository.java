package com.example.evostyle.domain.review.repository;

import com.example.evostyle.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewQueryRepository {
    List<Review> findByProductId(Long productId);

    Optional<Review> findByIdAndMemberId(Long reviewId, Long memberId);
}
