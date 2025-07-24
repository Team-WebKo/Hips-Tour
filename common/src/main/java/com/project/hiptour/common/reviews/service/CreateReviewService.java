package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    public Long create(CreateReviewRequestDto requestDto, User user) {
        Place place = placeRepository.findById(requestDto.getPlaceId()).orElseThrow(() -> new IllegalArgumentException("해당 장소는 존재하지 않습니다."));

        Review review = Review.builder()
                .place(place)
                .content(requestDto.getContent())
                .isLove(requestDto.getIsLove())
                .imageUrls(requestDto.getImageUrls())
                .user(user)
                .build();

        reviewRepository.save(review);

        return review.getReviewId();
    }
}
