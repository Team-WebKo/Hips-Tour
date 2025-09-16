package com.project.hiptour.common.reviews.serviceImpl;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.review.Review;
import com.project.hiptour.common.entity.review.repos.ReviewRepository;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
                .nickName("testUser")
                .email("test@example.com")
                .build();
        userInfo.setUserId(1L);

        place = Place.builder()
                .placeName("테스트 장소")
                .build();
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
                    .reviewId(1L)
                    .place(place)
                    .userInfo(userInfo)
                    .headText(requestDto.getHeadText())
                    .bodyText(requestDto.getBodyText())
                    .build();

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
}
