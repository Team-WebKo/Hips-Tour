package com.project.hiptour.common.application.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewPinRequestDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.global.exception.ReviewNotFoundException;
import com.project.hiptour.common.reviews.serviceImpl.ReviewServiceImpl;
import com.project.hiptour.common.users.entity.User;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Place place;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        place = new Place("테스트장소", "서울시 어딘가", "상세주소", null);
        setPrivateField(place, "id", 1L);

        user = User.class.getDeclaredConstructor().newInstance();
        setPrivateField(user, "userId", 101L);
        setPrivateField(user, "nickname", "테스터");
    }

    @Test
    @DisplayName("ex) offset=0, limit=2 → 첫 2개 리뷰를 반환")
    void getReviews_firstPage() {
        // given
        Long placeId = 1L;
        int offset = 0;
        int limit  = 2;

        Review r1 = Review.builder()
                .reviewId(10L)
                .place(place)
                .user(user)
                .content("리뷰1")
                .isLove(true)
                .imageUrls(List.of("a.jpg"))
                .build();

        Review r2 = Review.builder()
                .reviewId(11L)
                .place(place)
                .user(user)
                .content("리뷰2")
                .isLove(false)
                .imageUrls(List.of("b.jpg", "c.jpg"))
                .build();

        Pageable pageable = PageRequest.of(offset, limit);

        given(placeRepository.findById(placeId)).willReturn(Optional.of(place));
        given(reviewRepository.findByPlaceIdOrderedWithOffsetLimit(placeId, pageable))
                .willReturn(List.of(r1, r2));

        // when
        List<ReviewListResponseDto> result = reviewService.getReviewsByPlaceId(placeId, offset, limit);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getReviewId()).isEqualTo(10L);
        assertThat(result.get(0).getContent()).isEqualTo("리뷰1");
        assertThat(result.get(0).getIsLove()).isTrue();
        assertThat(result.get(0).getImageUrls()).containsExactly("a.jpg");
        assertThat(result.get(0).getUserId()).isEqualTo(101L);
        assertThat(result.get(0).getNickname()).isEqualTo("테스터");

        assertThat(result.get(1).getReviewId()).isEqualTo(11L);
        assertThat(result.get(1).getIsLove()).isFalse();
        assertThat(result.get(1).getImageUrls()).containsExactly("b.jpg", "c.jpg");

        verify(placeRepository, times(1)).findById(placeId);
        verify(reviewRepository, times(1)).findByPlaceIdOrderedWithOffsetLimit(placeId, pageable);
        verifyNoMoreInteractions(placeRepository, reviewRepository);
    }

    @Test
    @DisplayName("ex) offset=2, limit=2 → 다음 페이지(추가 2개)를 반환")
    void getReviews_nextPage() {
        // given
        Long placeId = 1L;
        int offset = 2;
        int limit  = 2;

        Review r3 = Review.builder()
                .reviewId(12L)
                .place(place)
                .user(user)
                .content("리뷰3")
                .isLove(true)
                .imageUrls(List.of())
                .build();

        Pageable pageable = PageRequest.of(offset, limit);

        given(placeRepository.findById(placeId)).willReturn(Optional.of(place));
        given(reviewRepository.findByPlaceIdOrderedWithOffsetLimit(placeId, pageable))
                .willReturn(List.of(r3));

        // when
        List<ReviewListResponseDto> result = reviewService.getReviewsByPlaceId(placeId, offset, limit);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReviewId()).isEqualTo(12L);
        assertThat(result.get(0).getContent()).isEqualTo("리뷰3");

        verify(placeRepository, times(1)).findById(placeId);
        verify(reviewRepository, times(1)).findByPlaceIdOrderedWithOffsetLimit(placeId, pageable);
        verifyNoMoreInteractions(placeRepository, reviewRepository);
    }

    @Test
    @DisplayName("존재하지 않는 placeId 로 요청 시 PlaceNotFoundException 발생")
    void getReviews_placeNotFound() {
        Long invalidPlaceId = 999L;
        given(placeRepository.findById(invalidPlaceId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewsByPlaceId(invalidPlaceId, 0, 5))
                .isInstanceOf(PlaceNotFoundException.class);

        verify(placeRepository, times(1)).findById(invalidPlaceId);
        verifyNoInteractions(reviewRepository);
    }

    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("필드 주입 실패: " + fieldName, e);
        }
    }

    @Test
    @DisplayName("리뷰가 존재하지 않으면 ReviewNotFoundException 발생")
    void pinReview_reviewNotFound() {
        // given
        Long nonExistentReviewId = 999L;
        ReviewPinRequestDto requestDto = new ReviewPinRequestDto();
        requestDto.setPinned(true);

        given(reviewRepository.findById(nonExistentReviewId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.pinReview(nonExistentReviewId, requestDto))
                .isInstanceOf(ReviewNotFoundException.class);

        verify(reviewRepository, times(1)).findById(nonExistentReviewId);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    void 리뷰_핀_지정_성공() {
        // given
        Long reviewId = 1L;
        ReviewPinRequestDto requestDto = new ReviewPinRequestDto(true);
        Review review = Mockito.spy(Review.builder()
                .reviewId(reviewId)
                .place(place)
                .user(user)
                .content("내용")
                .isLove(true)
                .imageUrls(List.of())
                .build());
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        reviewService.pinReview(reviewId, requestDto);

        // then
        verify(review, times(1)).pin();
        verify(review, never()).unpin();
    }

    @Test
    void 리뷰_핀_해제_성공() {
        // given
        Long reviewId = 1L;
        ReviewPinRequestDto requestDto = new ReviewPinRequestDto(false);
        Review review = Mockito.spy(Review.builder()
                .reviewId(reviewId)
                .place(place)
                .user(user)
                .content("내용")
                .isLove(true)
                .imageUrls(List.of())
                .build());
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        reviewService.pinReview(reviewId, requestDto);

        // then
        verify(review, times(1)).unpin();
        verify(review, never()).pin();
    }

    @Test
    void 존재하지_않는_리뷰에_핀_설정_시도시_예외_발생() {
        // given
        Long reviewId = 999L;
        ReviewPinRequestDto requestDto = new ReviewPinRequestDto(true);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ReviewNotFoundException.class, () ->
                reviewService.pinReview(reviewId, requestDto));
    }
}