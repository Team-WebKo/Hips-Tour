package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public void update(Long reviewId, UpdateReviewRequestDto dto, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("본인의 리뷰만 수정이 가능합니다.");
        }

        review.update(dto.getContent(), dto.getIsLove(), dto.getImageUrls());
    }
}
