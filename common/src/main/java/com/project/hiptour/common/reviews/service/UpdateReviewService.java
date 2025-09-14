package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public void update(Long reviewId, UpdateReviewRequestDto dto, UserInfo userInfo) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getUserInfo().getUserId().equals(userInfo.getUserId())) {
            throw new IllegalArgumentException("본인의 리뷰만 수정이 가능합니다.");
        }

        review.update(dto.getHeadText(), dto.getBodyText(), dto.getImageUrls(), dto.getHashTags());
    }
}
