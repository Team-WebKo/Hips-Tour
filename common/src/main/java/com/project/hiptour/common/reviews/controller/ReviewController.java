package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.dto.MyReviewResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewPinRequestDto;
import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import com.project.hiptour.common.reviews.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewService createReviewService;
    private final ReviewService reviewService;
    private final UpdateReviewService updateReviewService;
    private final DeleteReviewService deleteReviewService;
    private final MyReviewService myReviewService;


    @Operation(summary = "리뷰 목록 조회", description = "장소 ID(placeId)에 해당하는 리뷰 offset, limit 나눠 조회")
    @GetMapping("/place/{placeId}")
    public List<ReviewListResponseDto> getReviewsByPlaceId(
            @Parameter(description = "장소 ID") @PathVariable Long placeId,
            @Parameter(description = "처음 가져올 개수") @RequestParam int offset,
            @Parameter(description = "가져올 개수") @RequestParam int limit
    ) {
        return reviewService.getReviewsByPlaceId(placeId, offset, limit);
    }
    @Operation(summary = "리뷰 핀 고정/해제", description = "특정 리뷰의 핀 상태를 수정한다.")
    @PatchMapping("/{reviewId}/pin")
    public ResponseEntity<Void> updateReviewPinStatus(
            @PathVariable Long reviewId,
            @RequestBody ReviewPinRequestDto requestDto
    ) {
        reviewService.pinReview(reviewId, requestDto);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Operation(summary = "리뷰 작성")
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestBody CreateReviewRequestDto requestDto
            // TODO: 인증 기능 추가 후 주석 제거 필요
    ) {
        // TODO: 인증 기능 추가 전까지 임시 UserInfo 객체 생성. 실제 인증된 사용자로 교체 필요
        UserInfo userInfo = new UserInfo();
        Long reviewId = createReviewService.create(requestDto, userInfo);
        return ResponseEntity.ok(Map.of("reviewId", reviewId));
    }

    @Operation(summary = "내 리뷰 목록 조회")
    @GetMapping("/my")
    public ResponseEntity<List<MyReviewResponseDto>> getMyReviews(
            // TODO: 인증 기능 추가 후 주석 제거 필요
    ) {
        // TODO: 인증 기능 추가 전까지 임시 UserInfo 객체 생성. 실제 인증된 사용자로 교체 필요
        UserInfo userInfo = new UserInfo();

        List<MyReviewResponseDto> myReviews = myReviewService.getMyReview(userInfo);
        return ResponseEntity.ok(myReviews);
    }

    @Operation(summary = "리뷰 수정")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto requestDto
            // TODO: 인증 기능 추가 후 주석 제거 필요
    ) {
        // TODO: 인증 기능 추가 전까지 임시 UserInfo 객체 생성. 실제 인증된 사용자로 교체 필요
        UserInfo userInfo = new UserInfo();
        updateReviewService.update(reviewId, requestDto, userInfo);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId
            // TODO: 인증 기능 추가 후 주석 제거 필요
    ) {
        // TODO: 인증 기능 추가 전까지 임시 UserInfo 객체 생성. 실제 인증된 사용자로 교체 필요
        UserInfo userInfo = new UserInfo();
        deleteReviewService.delete(reviewId, userInfo);
        return ResponseEntity.noContent().build();
    }
}
