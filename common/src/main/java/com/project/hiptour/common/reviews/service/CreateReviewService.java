package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public Long create(CreateReviewRequestDto requestDto, UserInfo userInfo) {
        Place place = placeRepository.findById(requestDto.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("해당 장소는 존재하지 않습니다."));

        Review review = Review.builder()
                .place(place)
                .userInfo(userInfo)
                .headText(requestDto.getHeadText())
                .bodyText(requestDto.getBodyText())
                .hashTags(requestDto.getHashTags())
                .build();

        reviewRepository.save(review);

        return review.getReviewId();
    }
}