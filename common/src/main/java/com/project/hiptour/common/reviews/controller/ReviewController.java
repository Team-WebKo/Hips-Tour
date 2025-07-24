package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.service.CreateReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final CreateReviewService createReviewService;
    private final PlaceRepository placeRepository;

    @PostMapping("/places/{placeId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable Long placeId,
            @RequestBody CreateReviewRequestDto reviewRequestDto
            ) {
        Long reviewId = createReviewService.create(placeId, reviewRequestDto, );
    }
}
