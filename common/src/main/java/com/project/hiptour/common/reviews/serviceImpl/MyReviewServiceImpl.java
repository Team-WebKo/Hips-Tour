package com.project.hiptour.common.reviews.serviceImpl;

import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.MyReviewResponseDto;
import com.project.hiptour.common.reviews.service.MyReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyReviewServiceImpl implements MyReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public List<MyReviewResponseDto> getMyReview(UserInfo userInfo) {
        List<Review> myReviews = reviewRepository.findByUserInfoOrderByCreatedAtDesc(userInfo);

        return myReviews.stream()
                .map(MyReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}
