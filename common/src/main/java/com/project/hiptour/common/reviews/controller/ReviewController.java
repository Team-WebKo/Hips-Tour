package com.project.hiptour.common.reviews.controller;

import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.service.CreateReviewService;
import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final CreateReviewService createReviewService;
    private final PlaceRepository placeRepository;

    @PostMapping("/places/{placeId}/reviews")
    public ResponseEntity<?> createReview(
            @RequestBody CreateReviewRequestDto reviewRequestDto
            // 유저 인증 정보 필요
            ) {
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
}
