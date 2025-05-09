package com.example.evostyle.domain.review.controller;

import com.example.evostyle.domain.review.dto.request.CreateReviewRequest;
import com.example.evostyle.domain.review.dto.request.UpdateReviewRequest;
import com.example.evostyle.domain.review.dto.response.CreateReviewResponse;
import com.example.evostyle.domain.review.dto.response.ReadReviewResponse;
import com.example.evostyle.domain.review.dto.response.UpdateReviewResponse;
import com.example.evostyle.domain.review.service.ReviewService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/order-items/{orderItemId}/reviews")
    public ResponseEntity<CreateReviewResponse> createReview(
        @PathVariable(name = "orderItemId") Long orderItemId,
        @RequestBody CreateReviewRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        CreateReviewResponse reviewResponse = reviewService.createReview(memberId, orderItemId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ReadReviewResponse>> readAllReviews(
        @PathVariable(name = "productId") Long productId
    ) {
        List<ReadReviewResponse> reviewResponseList = reviewService.readAllReviews(productId);

        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseList);
    }

    @PatchMapping("/products/reviews/{reviewId}")
    public ResponseEntity<UpdateReviewResponse> updateReview(
        @PathVariable(name = "reviewId") Long reviewId,
        @RequestBody UpdateReviewRequest request,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        UpdateReviewResponse updateReviewResponse = reviewService.updateReview(memberId, reviewId, request);

        return ResponseEntity.status(HttpStatus.OK).body(updateReviewResponse);
    }

    @DeleteMapping("/products/reviews/{reviewId}")
    public ResponseEntity<Map<String, Long>> deleteReview(
        @PathVariable(name = "reviewId") Long reviewId,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        reviewService.deleteReview(memberId, reviewId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("reviewId", reviewId));
    }
}
