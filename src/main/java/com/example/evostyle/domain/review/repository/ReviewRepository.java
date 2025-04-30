package com.example.evostyle.domain.review.repository;

import com.example.evostyle.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.orderItem.productDetail.product.id = :productId")
    List<Review> findByProductId(Long productId);

    @Query("SELECT r FROM Review r WHERE r.id = :reviewId AND r.member.id = :memberId AND r.orderItem.productDetail.product.id = :productId")
    Optional<Review> findByIdAndMemberIdAndProductId(Long reviewId, Long memberId, Long productId);
}
