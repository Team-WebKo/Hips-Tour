package com.project.hiptour.common.reviews.service;


import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Override
    public List<ReviewListResponseDto> getReviewsByPlaceId(Long placeId, int offset, int limit) {
        // place 확인
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(placeId));

        // 조회
        List<Review> reviews = reviewRepository.findByPlaceIdWithOffsetLimit(placeId, offset, limit);

        return reviews.stream()
                .map(review -> {
                    ReviewListResponseDto dto = new ReviewListResponseDto();
                    dto.setReviewId(review.getReviewId());
                    dto.setContent(review.getContent());
                    dto.setIsLove(review.getIsLove());
                    dto.setImageUrls(review.getImageUrls());
                    dto.setUserId(review.getUser().getUserId());
                    dto.setNickname(review.getUser().getNickname());
                    return dto;
                })
                .toList();
    }
}
