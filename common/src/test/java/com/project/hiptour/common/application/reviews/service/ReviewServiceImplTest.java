package com.project.hiptour.common.application.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.service.ReviewServiceImpl;
import com.project.hiptour.common.users.entity.User;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;



import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Place place;  // 실제 엔티티 사용 (리플렉션으로 id 주입)
    private User user;    // 실제 엔티티 사용 (리플렉션으로 userId/nickname 주입)

    @BeforeEach
    void setUp() throws Exception {
        place = new Place("테스트장소", "서울시 어딘가", "상세주소", null);
        // 테스트id
        setPrivateField(place, "id", 1L);

        user = User.class.getDeclaredConstructor().newInstance();
        setPrivateField(user, "userId", 101L);
        setPrivateField(user, "nickname", "테스터");
    }

    @Test
    @DisplayName("offset=0, limit=2 → 첫 2개 리뷰를 반환한다")
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

        given(placeRepository.findById(placeId)).willReturn(Optional.of(place));
        given(reviewRepository.findByPlaceIdWithOffsetLimit(placeId, offset, limit))
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
        verify(reviewRepository, times(1)).findByPlaceIdWithOffsetLimit(placeId, offset, limit);
        verifyNoMoreInteractions(placeRepository, reviewRepository);
    }

    @Test
    @DisplayName("offset=2, limit=2 → 다음 페이지(추가 2개)를 반환한다")
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

        given(placeRepository.findById(placeId)).willReturn(Optional.of(place));
        given(reviewRepository.findByPlaceIdWithOffsetLimit(placeId, offset, limit))
                .willReturn(List.of(r3));

        // when
        List<ReviewListResponseDto> result = reviewService.getReviewsByPlaceId(placeId, offset, limit);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReviewId()).isEqualTo(12L);
        assertThat(result.get(0).getContent()).isEqualTo("리뷰3");

        verify(placeRepository, times(1)).findById(placeId);
        verify(reviewRepository, times(1)).findByPlaceIdWithOffsetLimit(placeId, offset, limit);
        verifyNoMoreInteractions(placeRepository, reviewRepository);
    }

    @Test
    @DisplayName("존재하지 않는 placeId 로 요청 시 PlaceNotFoundException 발생")
    void getReviews_placeNotFound() {
        // given
        Long invalidPlaceId = 999L;
        given(placeRepository.findById(invalidPlaceId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.getReviewsByPlaceId(invalidPlaceId, 0, 5))
                .isInstanceOf(PlaceNotFoundException.class);

        verify(placeRepository, times(1)).findById(invalidPlaceId);
        verifyNoInteractions(reviewRepository);
    }

    /**
     * 테스트에서만 사용하는 리플렉션 유틸
     * - 엔티티의 private 필드(id, userId 등)를 강제로 주입
     */
    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("필드를 찾을 수 없습니다. 대상=" + target.getClass() + ", 필드명=" + fieldName, e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("필드 접근에 실패했습니다. 대상=" + target.getClass() + ", 필드명=" + fieldName, e);
        }
    }
}
