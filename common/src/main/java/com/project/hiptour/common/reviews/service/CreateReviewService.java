package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.ReviewRequestDto;
import com.project.hiptour.common.reviews.dto.ReviewResponseDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
        Place place = placeRepository.findById(requestDto.getPlaceId()).orElseThrow(() -> new IllegalArgumentException("해당 장소는 존재하지 않습니다."));

        Review review = Review.builder()
                .place(place)
                .
    }
}
