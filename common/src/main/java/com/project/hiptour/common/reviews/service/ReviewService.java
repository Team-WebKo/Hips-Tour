package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewPinRequestDto;

import java.util.List;

public interface ReviewService {
    List<ReviewListResponseDto> getReviewsByPlaceId(int placeId, int offset, int limit);
    void pinReview(Long reviewId, ReviewPinRequestDto requestDto);
}
