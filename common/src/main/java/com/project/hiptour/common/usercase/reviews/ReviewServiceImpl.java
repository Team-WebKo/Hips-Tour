package com.project.hiptour.common.usercase.reviews;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.exception.place.PlaceNotFoundException;
import com.project.hiptour.common.exception.review.ReviewAccessDeniedException;
import com.project.hiptour.common.exception.review.ReviewNotFoundException;
import com.project.hiptour.common.util.PageResponseDto;
import com.project.hiptour.common.web.reviews.CreateReviewRequestDto;
import com.project.hiptour.common.web.reviews.MyReviewResponseDto;
import com.project.hiptour.common.web.reviews.ReviewListResponseDto;
import com.project.hiptour.common.web.reviews.UpdateReviewRequestDto;
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

    /**
     * 새로운 리뷰를 생성합니다.
     *
     * @param requestDto 생성할 리뷰의 내용을 담은 DTO
     * @param placeId    리뷰를 작성할 장소의 ID
     * @param userInfo   리뷰를 작성하는 사용자 정보
     * @return 생성된 리뷰의 ID
     * @throws PlaceNotFoundException 요청한 ID에 해당하는 장소를 찾을 수 없을 경우
     */
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

    /**
     * 기존 리뷰를 수정합니다.
     *
     * @param reviewId   수정할 리뷰의 ID
     * @param requestDto 수정할 리뷰의 내용을 담은 DTO
     * @throws ReviewNotFoundException     요청한 ID에 해당하는 리뷰를 찾을 수 없을 경우
     * @throws ReviewAccessDeniedException 리뷰를 수정할 권한이 없을 경우
     */
    @Override
    @Transactional
    public void update(Long reviewId, UpdateReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));

        review.update(requestDto.getHeadText(), requestDto.getBodyText(), requestDto.getImageUrls(), requestDto.getHashTags());
    }

    /**
     * 리뷰를 삭제합니다.
     *
     * @param reviewId 삭제할 리뷰의 ID
     * @throws ReviewNotFoundException     요청한 ID에 해당하는 리뷰를 찾을 수 없을 경우
     * @throws ReviewAccessDeniedException 리뷰를 삭제할 권한이 없을 경우
     */
    @Override
    @Transactional
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));

        reviewRepository.delete(review);
    }

    /**
     * 특정 장소에 작성된 리뷰 목록을 페이징하여 조회합니다.
     *
     * @param placeId  리뷰를 조회할 장소의 ID
     * @param pageable 페이징 정보
     * @return 페이징된 리뷰 목록
     * @throws PlaceNotFoundException 요청한 ID에 해당하는 장소를 찾을 수 없을 경우
     */
    @Override
    public PageResponseDto<ReviewListResponseDto> getReviewsByPlace(Integer placeId, Pageable pageable) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("장소를 찾을 수 없습니다: " + placeId));

        Page<Review> reviewPage = reviewRepository.findByPlace(place, pageable);
        return PageResponseDto.fromPage(reviewPage, ReviewListResponseDto::from);
    }

    /**
     * 특정 사용자가 작성한 모든 리뷰 목록을 페이징하여 조회합니다.
     *
     * @param userInfo 리뷰를 조회할 사용자 정보
     * @param pageable 페이징 정보
     * @return 페이징된 "내가 쓴 리뷰" 목록
     */
    @Override
    public PageResponseDto<MyReviewResponseDto> getMyReviews(UserInfo userInfo, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByUserInfo(userInfo, pageable);
        return PageResponseDto.fromPage(reviewPage, MyReviewResponseDto::from);
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
