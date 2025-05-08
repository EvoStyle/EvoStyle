package com.example.evostyle.domain.review.repository;

import com.example.evostyle.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {

    boolean existsByMemberIdAndOrderItemIdAndIsDeletedFalse(Long memberId, Long orderItemId);
}
