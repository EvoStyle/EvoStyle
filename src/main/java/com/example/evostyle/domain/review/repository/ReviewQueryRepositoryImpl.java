package com.example.evostyle.domain.review.repository;

import com.example.evostyle.domain.review.entity.QReview;
import com.example.evostyle.domain.review.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findByProductId(Long productId) {
        QReview review = QReview.review;

        return queryFactory
            .selectFrom(review)
            .where(review.orderItem.productDetail.product.id.eq(productId))
            .fetch();
    }

    @Override
    public Optional<Review> findByIdAndMemberId(Long reviewId, Long memberId) {
        QReview review = QReview.review;

        return Optional.ofNullable(
            queryFactory
            .selectFrom(review)
            .where(review.id.eq(reviewId)
                .and(review.member.id.eq(memberId)))
            .fetchOne());
    }
}
