package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.repos.UserRepos;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.dto.MyReviewResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import com.project.hiptour.common.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepos userRepos;

    @Operation(summary = "리뷰 작성", description = "특정 장소 url을 파라미터로 받아 사용")
    @PostMapping("/places/{placeId}/reviews")
    public ResponseEntity<?> createReview(@PathVariable Integer placeId, @RequestBody CreateReviewRequestDto requestDto) {
        UserInfo userInfo = userRepos.findById(requestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + requestDto.getUserId()));

        Long reviewId = reviewService.create(requestDto, placeId, userInfo);
        return ResponseEntity.ok(Map.of("reviewId", reviewId));
    }

    @Operation(summary = "리뷰 목록 조회", description = "장소에 대한 모든 리뷰를 조회합니다.")
    @GetMapping("/places/{placeId}/reviews")
    public Page<ReviewListResponseDto> getReviewByPlace(
            @PathVariable Integer placeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return reviewService.getReviewsByPlace(placeId, pageable);
    }

    @Operation(summary = "내가 작성한 리뷰 목록 조회", description = "내가 작성한 리뷰 목록만 조회합니다. 수정 및 삭제에 활용")
    @GetMapping("reviews/my")
    public Page<MyReviewResponseDto> getMyReviews(
            @RequestParam Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        UserInfo userInfo = userRepos.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        return reviewService.getMyReviews(userInfo, pageable);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 수정을 위해서는 우선 내가 작성한 리뷰 목록을 조회해야 합니다.")
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto requestDto
            ) {
        UserInfo userInfo = userRepos.findById(requestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + requestDto.getUserId()));

        reviewService.update(reviewId, requestDto, userInfo);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제를 위해선 내가 작성한 리뷰 목록을 조회해야 합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId
    ) {
        UserInfo userInfo = userRepos.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        reviewService.delete(reviewId, userInfo);
        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "리뷰 목록 조회", description = "장소 ID(placeId)에 해당하는 리뷰를 페이징하여 조회")
//    @GetMapping("/reviews/place/{placeId}")
//    public Page<ReviewListResponseDto> getReviewsByPlaceId(@PathVariable int placeId, Pageable pageable) {
//        Pageable sortedPageable = PageRequest.of(
//                pageable.getPageNumber(),
//                pageable.getPageSize(),
//                Sort.by(""
//    }

//    @Operation(summary = "리뷰 핀 고정/해제", description = "특정 리뷰의 핀 상태를 수정한다.")
//    @PatchMapping("/reviews/{reviewId}/pin")
//    public ResponseEntity<Void> updateReviewPinStatus(
//            @PathVariable Long reviewId,
//            @RequestBody ReviewPinRequestDto requestDto
//    ) {
//        reviewService.pinReview(reviewId, requestDto);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }
}