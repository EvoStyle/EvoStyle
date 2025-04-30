package com.example.evostyle.domain.review.service;

import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.repository.OrderItemRepository;
import com.example.evostyle.domain.review.dto.request.CreateReviewRequest;
import com.example.evostyle.domain.review.dto.request.UpdateReviewRequest;
import com.example.evostyle.domain.review.dto.response.CreateReviewResponse;
import com.example.evostyle.domain.review.dto.response.ReadReviewResponse;
import com.example.evostyle.domain.review.dto.response.UpdateReviewResponse;
import com.example.evostyle.domain.review.entity.Review;
import com.example.evostyle.domain.review.repository.ReviewRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public CreateReviewResponse createReview(Long memberId, Long orderItemId, CreateReviewRequest request) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        Review review = Review.of(request.title(), request.rating(), request.contents(), member, orderItem);

        reviewRepository.save(review);

        return CreateReviewResponse.from(review);
    }

    public List<ReadReviewResponse> readAllReviews(Long productId) {
        List<Review> reviewList = reviewRepository.findByProductId(productId);

        return reviewList.stream()
            .map(ReadReviewResponse::from)
            .toList();
    }

    @Transactional
    public UpdateReviewResponse updateReview(Long memberId, Long productId, Long reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findByIdAndMemberIdAndProductId(reviewId, memberId, productId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        review.update(request.title(), request.rating(), request.contents());

        return UpdateReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(Long memberId, Long productId, Long reviewId) {
        Review review = reviewRepository.findByIdAndMemberIdAndProductId(reviewId, memberId, productId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        review.delete();
    }
}
