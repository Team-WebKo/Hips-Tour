package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public void delete(Long reviewId, UserInfo userInfo) {
        Review target = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        if (!target.getUserInfo().getUserId().equals(userInfo.getUserId())) {
            throw new IllegalArgumentException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(target);
    }
}
