package com.project.hiptour.common.reviews.serviceImpl;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.exception.place.PlaceNotFoundException;
import com.project.hiptour.common.exception.review.ReviewAccessDeniedException;
import com.project.hiptour.common.exception.review.ReviewNotFoundException;
import com.project.hiptour.common.usercase.reviews.ReviewServiceImpl;
import com.project.hiptour.common.web.reviews.CreateReviewRequestDto;
import com.project.hiptour.common.web.reviews.MyReviewResponseDto;
import com.project.hiptour.common.web.reviews.ReviewListResponseDto;
import com.project.hiptour.common.web.reviews.UpdateReviewRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService 단위 테스트")
public class ReviewServiceImplTest {
    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PlaceRepository placeRepository;

    private UserInfo userInfo;
    private Place place;

    @BeforeEach
    void init() {
        userInfo = UserInfo.builder()
                .email("test@example.com")
                .nickName("testUser")
                .build();
        userInfo.setUserId(1L);

        place = Place.builder()
                .placeName("테스트 장소")
                .build();
        place.setPlaceId(1);
    }

    @Nested
    @DisplayName("리뷰 생성 기능")
    class CreateReviewTest {
        @Test
        @DisplayName("성공")
        void success() {
            CreateReviewRequestDto requestDto = CreateReviewRequestDto.builder()
                    .headText("제목")
                    .bodyText("내용")
                    .build();

            Integer placeId = 1;
            place.setPlaceId(placeId);

            Review savedReview = Review.builder()
                    .place(place)
                    .userInfo(userInfo)
                    .headText(requestDto.getHeadText())
                    .bodyText(requestDto.getBodyText())
                    .build();
            savedReview.setReviewId(1L);

            given(placeRepository.findById(placeId)).willReturn(Optional.of(place));
            given(reviewRepository.save(any(Review.class))).willReturn(savedReview);

            Long createdReviewId = reviewService.create(requestDto, placeId, userInfo);

            assertThat(createdReviewId).isEqualTo(1L);
            verify(placeRepository).findById(placeId);
            verify(reviewRepository).save(any(Review.class));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 장소")
        void fail_place_not_found() {
            CreateReviewRequestDto requestDto = CreateReviewRequestDto.builder().build();
            Integer notExistPlaceId = 999;
            given(placeRepository.findById(notExistPlaceId)).willReturn(Optional.empty());

            assertThrows(PlaceNotFoundException.class, () -> {
                reviewService.create(requestDto, notExistPlaceId, userInfo);
            });
        }
    }

    @Nested
    @DisplayName("특정 장소 리뷰 목록 조회")
    class GetReviewsByPlaceTest {
        @Test
        @DisplayName("성공")
        void success() {
            Integer placeId = 1;
            Pageable pageable = PageRequest.of(0, 10);

            Review review1 = Review.builder().headText("리뷰제목1").bodyText("내용1").userInfo(userInfo).place(place).build();
            review1.setReviewId(1L);
            Review review2 = Review.builder().headText("리뷰제목2").bodyText("내용2").userInfo(userInfo).place(place).build();
            review2.setReviewId(2L);
            List<Review> reviewList = List.of(review1, review2);

            Page<Review> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());

            given(placeRepository.findById(placeId)).willReturn(Optional.of(place));
            given(reviewRepository.findByPlace(place, pageable)).willReturn(reviewPage);

            Page<ReviewListResponseDto> result = reviewService.getReviewsByPlace(placeId, pageable);

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getContent().get(0).getHeadText()).isEqualTo("리뷰제목1");

            verify(placeRepository).findById(placeId);
            verify(reviewRepository).findByPlace(place, pageable);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 장소")
        void fail_place_not_found() {
            Integer notExistPlaceId = 999;
            Pageable pageable = PageRequest.of(0, 10);

            given(placeRepository.findById(notExistPlaceId)).willReturn(Optional.empty());

            assertThrows(PlaceNotFoundException.class, () -> {
                reviewService.getReviewsByPlace(notExistPlaceId, pageable);
            });
        }
    }

    @Nested
    @DisplayName("리뷰 수정")
    class UpdateReviewTest {
        private UpdateReviewRequestDto updateDto;
        private UserInfo anotherUser;

        @BeforeEach
        void init() {
            updateDto = UpdateReviewRequestDto.builder()
                    .headText("수정된 제목")
                    .bodyText("수정된 내용")
                    .build();

            anotherUser = UserInfo.builder().build();
            anotherUser.setUserId(2L);
        }

        @Test
        @DisplayName("성공")
        void success() {
            Long reviewId = 1L;
            Review mockReview = mock(Review.class);

            given(mockReview.getUserInfo()).willReturn(userInfo);
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(mockReview));

            reviewService.update(reviewId, updateDto, userInfo);

            verify(reviewRepository).findById(reviewId);
            verify(mockReview).update(updateDto.getHeadText(), updateDto.getBodyText(), updateDto.getImageUrls(), updateDto.getHashTags());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void fail_review_not_found() {
            Long notExistReviewId = 999L;

            given(reviewRepository.findById(notExistReviewId)).willReturn(Optional.empty());

            assertThrows(ReviewNotFoundException.class, () -> {
                reviewService.update(notExistReviewId, updateDto, userInfo);
            });
        }

        @Test
        @DisplayName("실패 - 수정 권한 없음")
        void fail_access_denied() {
            Long reviewId = 1L;

            Review review = Review.builder().userInfo(userInfo).build();

            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

            assertThrows(ReviewAccessDeniedException.class, () -> {
                reviewService.update(reviewId, updateDto, anotherUser);
            });
        }
    }

    @Nested
    @DisplayName("리뷰 삭제")
    class DeleteReviewTest {
        private UserInfo anotherUser;

        @BeforeEach
        void init() {
            anotherUser = UserInfo.builder().build();
            anotherUser.setUserId(2L);
        }

        @Test
        @DisplayName("성공")
        void success() {
            Long reviewId = 1L;
            Review mockReview = mock(Review.class);

            given(mockReview.getUserInfo()).willReturn(userInfo);
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(mockReview));

            reviewService.delete(reviewId, userInfo);

            verify(reviewRepository).findById(reviewId);
            verify(reviewRepository).delete(mockReview);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리뷰")
        void fail_review_not_found() {
            Long notExistReviewId = 999L;

            given(reviewRepository.findById(notExistReviewId)).willReturn(Optional.empty());

            assertThrows(ReviewNotFoundException.class, () -> {
                reviewService.delete(notExistReviewId, userInfo);
            });
        }

        @Test
        @DisplayName("실패 - 삭제 권한 없음")
        void fail_access_denied() {
            Long reviewId = 1L;
            Review mockReview = mock(Review.class);

            given(mockReview.getUserInfo()).willReturn(userInfo);
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(mockReview));

            assertThrows(ReviewAccessDeniedException.class, () -> {
                reviewService.delete(reviewId, anotherUser);
            });
        }
    }

    @Nested
    @DisplayName("내가 작성한 리뷰 목록 조회")
    class GetMyReviewTest {
        @Test
        @DisplayName("성공")
        void success() {
            Pageable pageable = PageRequest.of(0, 5);

            Review review1 = Review.builder().userInfo(userInfo).place(place).headText("내 리뷰 1").build();
            review1.setReviewId(1L);
            Review review2 = Review.builder().userInfo(userInfo).place(place).headText("내 리뷰 2").build();
            review2.setReviewId(2L);
            List<Review> myReviewList = List.of(review1, review2);

            Page<Review> reviewPage = new PageImpl<>(myReviewList, pageable, myReviewList.size());

            given(reviewRepository.findByUserInfo(userInfo, pageable)).willReturn(reviewPage);

            Page<MyReviewResponseDto> result = reviewService.getMyReviews(userInfo, pageable);

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getContent().get(0).getHeadText()).isEqualTo("내 리뷰 1");

            verify(reviewRepository).findByUserInfo(userInfo, pageable);
        }

        @Test
        @DisplayName("작성한 리뷰가 없는 경우 빈 페이지 반환")
        void success_when_empty() {
            Pageable pageable = PageRequest.of(0, 5);

            given(reviewRepository.findByUserInfo(userInfo, pageable)).willReturn(Page.empty(pageable));

            Page<MyReviewResponseDto> result = reviewService.getMyReviews(userInfo, pageable);

            assertThat(result).isNotNull();
            assertThat(result.isEmpty()).isTrue();

            verify(reviewRepository).findByUserInfo(userInfo, pageable);
        }
    }
}
