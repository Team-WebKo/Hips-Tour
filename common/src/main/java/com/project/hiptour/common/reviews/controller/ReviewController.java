package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.service.CreateReviewService;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.project.hiptour.common.reviews.service.DeleteReviewService;
import com.project.hiptour.common.reviews.service.ReviewQueryService;
import com.project.hiptour.common.reviews.service.UpdateReviewService;
import com.project.hiptour.common.users.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewService createReviewService;
    private final ReviewService reviewService;
    private final ReviewQueryService reviewQueryService;
    private final UpdateReviewService updateReviewService;
    private final DeleteReviewService deleteReviewService;


    @Operation(summary = "리뷰 목록 조회", description = "장소 ID(placeId)에 해당하는 리뷰 offset, limit 나눠 조회")
    @GetMapping("/place/{placeId}")
    public List<ReviewListResponseDto> getReviewsByPlaceId(
            @Parameter(description = "장소 ID") @PathVariable Long placeId,
            @Parameter(description = "처음 가져올 개수") @RequestParam int offset,
            @Parameter(description = "가져올 개수") @RequestParam int limit
    ) {
        return reviewService.getReviewsByPlaceId(placeId, offset, limit);

    @PostMapping("/places/{placeId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable Long placeId,
            @RequestBody CreateReviewRequestDto reviewRequestDto
            // 유저 인증 정보 필요
            ) {
        reviewRequestDto.setPlaceId(placeId);

        User user = new User(); /**유저 정보 및 인증 에 대한 추가 이후 방향 결정 필요**/
        Long reviewId = createReviewService.create(reviewRequestDto, user);
        return ResponseEntity.ok(Map.of("reviewId", reviewId));
/**
 * 현재 User 객체를 직접 생성하도록 되어있습니다.
 * 추후 유저 및 로그인 기능 추가 이후 작업 필요합니다.
 *
 *
 * **/
    }

    /**
     * 여행지 상세 페이제에서가 아닌 리뷰 작성 접근 후 여행지 선택에 대한 기능입니다.
     * **/
    @PostMapping("/reviews")
    public ResponseEntity<?> createReviewWithoutPlace(
            @RequestBody CreateReviewRequestDto dto
    ) {
        User user = new User(); //인증 대체
        Long reviewId = createReviewService.create(dto, user);
        return ResponseEntity.ok(Map.of("reviewId", reviewId));
    }

    /**
     * 자신이 작성한 리뷰에 대한 기능
     * **/
    @GetMapping("/places/{placeId}/my_review")
    public ResponseEntity<?> getMyReview(
            @PathVariable Long placeId
    ) {
        User user = new User();
        Optional<Review> review = reviewQueryService.findByUserAndPlace(user, placeId);

        return review.map(target -> ResponseEntity.ok(Map.of(
                "reviewId", target.getReviewId(),
                "content", target.getContent(),
                "imageUrls", target.getImageUrls()//이미지에 대한 내용 논의 필요
        ))).orElse(ResponseEntity.noContent().build());
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequestDto requestDto
            //사용자 인증 정보 필요
            ) {
        User user = new User();//임시 객체
        updateReviewService.update(reviewId, requestDto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId
            // 유저 인증 처리 필요
    ) {
        User user = new User();//임시 인증 객체
        deleteReviewService.delete(reviewId, user);
        return ResponseEntity.noContent().build();
    }
}
