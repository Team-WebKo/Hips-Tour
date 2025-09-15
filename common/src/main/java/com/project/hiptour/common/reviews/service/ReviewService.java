package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.dto.MyReviewResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Long create(CreateReviewRequestDto requestDto, Integer placeId, UserInfo userInfo);
    void update(Long reviewId, UpdateReviewRequestDto requestDto, UserInfo userInfo);
    void delete(Long reviewId, UserInfo userInfo);
    Page<ReviewListResponseDto> getReviewsByPlace(Integer placeId, Pageable pageable);
    Page<MyReviewResponseDto> getMyReviews(UserInfo userInfo, Pageable pageable);
//    Page<ReviewListResponseDto> getReviewsByPlaceId(int placeId, Pageable pageable);
//    void pinReview(Long reviewId, ReviewPinRequestDto requestDto);
}