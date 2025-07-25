package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;

import java.util.List;

public interface ReviewService {
    List<ReviewListResponseDto> getReviewsByPlaceId(Long placeId, int offset, int limit);
}
