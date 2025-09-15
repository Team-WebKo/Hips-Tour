package com.project.hiptour.common.reviews.serviceImpl;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.dto.MyReviewResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.UpdateReviewRequestDto;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import com.project.hiptour.common.reviews.global.exception.ReviewAccessDeniedException;
import com.project.hiptour.common.reviews.global.exception.ReviewNotFoundException;
import com.project.hiptour.common.reviews.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public Long create(CreateReviewRequestDto requestDto, Integer placeId, UserInfo userInfo) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("장소를 찾을 수 없습니다: " + placeId));

        Review review = Review.builder()
                .place(place)
                .userInfo(userInfo)
                .headText(requestDto.getHeadText())
                .bodyText(requestDto.getBodyText())
                .imageUrls(requestDto.getImageUrls())
                .hashTags(requestDto.getHashTags())
                .build();

        Review savedReview = reviewRepository.save(review);
        return savedReview.getReviewId();
    }

    @Override
    @Transactional
    public void update(Long reviewId, UpdateReviewRequestDto requestDto, UserInfo userInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));

        if (!review.getUserInfo().getUserId().equals(userInfo.getUserId())) {
            throw new ReviewAccessDeniedException("리뷰 수정 권한이 없습니다.");
        }

        review.update(requestDto.getHeadText(), requestDto.getBodyText(), requestDto.getImageUrls(), requestDto.getHashTags());
    }

    @Override
    @Transactional
    public void delete(Long reviewId, UserInfo userInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));

        if (!review.getUserInfo().getUserId().equals(userInfo.getUserId())) {
            throw new ReviewAccessDeniedException("리뷰 삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }

    @Override
    public Page<ReviewListResponseDto> getReviewsByPlace(Integer placeId, Pageable pageable) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("장소를 찾을 수 없습니다: " + placeId));

        Page<Review> reviewPage = reviewRepository.findByPlace(place, pageable);
        return reviewPage.map(ReviewListResponseDto::from);
    }

    @Override
    public Page<MyReviewResponseDto> getMyReviews(UserInfo userInfo, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByUserInfo(userInfo, pageable);
        return reviewPage.map(MyReviewResponseDto::from);
    }

    //    @Override
//    public Page<ReviewListResponseDto> getReviewsByPlaceId(int placeId, Pageable pageable) {
//        placeRepository.findById(placeId).orElseThrow(() -> new PlaceNotFoundException(placeId));
//
//        Page<Review> reviewPage = reviewRepository.findByPlaceIdOrdered(placeId, pageable);
//
//        List<Long> userIds = reviewPage.getContent().stream().map(Review::getUserId).distinct().toList();
//        Map<Long, UserInfo> userMap = userRepos.findAllByUserIdIn(userIds).stream()
//                .collect(Collectors.toMap(UserInfo::getUserId, u -> u));
//
//        return reviewPage.map(review -> ReviewListResponseDto.from(review, userMap.get(review.getUserId())));
//    }

//    @Override
//    @Transactional
//    public void pinReview(Long reviewId, ReviewPinRequestDto requestDto) {
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
//        if (requestDto.isPinned()) {
//            review.pin();
//        } else {
//            review.unpin();
//        }
//    }
}
