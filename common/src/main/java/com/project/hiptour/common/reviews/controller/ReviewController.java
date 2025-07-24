package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.reviews.service.CreateReviewService;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewService createReviewService;
    private final ReviewService reviewService;



    @Operation(summary = "리뷰 목록 조회", description = "장소 ID(placeId)에 해당하는 리뷰 offset, limit 나눠 조회")
    @GetMapping("/place/{placeId}")
    public List<ReviewListResponseDto> getReviewsByPlaceId(
            @Parameter(description = "장소 ID") @PathVariable Long placeId,
            @Parameter(description = "가져올 시작 위치") @RequestParam int offset,
            @Parameter(description = "가져올 개수") @RequestParam int limit
    ) {
        return reviewService.getReviewsByPlaceId(placeId, offset, limit);
    }
}
