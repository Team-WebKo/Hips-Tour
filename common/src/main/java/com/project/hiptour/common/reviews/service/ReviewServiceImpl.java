package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewListResponseDto> getReviewsByPlaceId(int placeId, int offset, int limit) {
        List<Review> reviews = reviewRepository.findByPlaceIdWithOffsetLimit(placeId, offset, limit);

        return reviews.stream()
                .map(review -> {
                    ReviewListResponseDto dto = new ReviewListResponseDto();
                    dto.setReviewId(review.getReviewId());
                    dto.setHeadText(review.getHeadText());
                    dto.setBodyText(review.getBodyText());
//                    dto.setIsLove(review.getIsLove());
                    dto.setImageUrls(review.getImageUrls());
                    dto.setUserId(dto.getUserId());
                    dto.setNickname(dto.getNickname());
                    return dto;
                })
                .toList();
    }
}
