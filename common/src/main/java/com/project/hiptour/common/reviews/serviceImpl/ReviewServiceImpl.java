package com.project.hiptour.common.reviews.serviceImpl;


import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewPinRequestDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import com.project.hiptour.common.reviews.global.exception.ReviewNotFoundException;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Override
    public List<ReviewListResponseDto> getReviewsByPlaceId(int placeId, int offset, int limit) {
        // 장소 존재 여부 확인
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(placeId));

        // 핀 우선 정렬 후 최신순 정렬 + 오프셋 기반 조회
        List<Review> reviews = reviewRepository.findByPlaceIdOrderedWithOffsetLimit(
                placeId,
                PageRequest.of(offset / limit, limit)
        );

        return reviews.stream()
                .map(review -> {
                    ReviewListResponseDto dto = new ReviewListResponseDto();
                    dto.setReviewId(review.getReviewId());
                    dto.setHeadText(review.getHeadText());
                    dto.setBodyText(review.getBodyText());
//                    dto.setIsLove(review.getIsLove());
                    dto.setImageUrls(review.getImageUrls());
                    dto.setUserId(review.getUserInfo().getUserId());
                    dto.setNickname(review.getUserInfo().getNickName());
                    dto.setPinned(review.getPinned());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public void pinReview(Long reviewId, ReviewPinRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        if (requestDto.isPinned()) {
            review.pin();     // pinned = true, pinnedAt = now()
        } else {
            review.unpin();   // pinned = false, pinnedAt= null
        }
    }

}
