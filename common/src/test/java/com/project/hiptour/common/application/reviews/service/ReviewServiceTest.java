package com.project.hiptour.common.application.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.CreateReviewRequestDto;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.reviews.service.CreateReviewService;
import com.project.hiptour.common.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private CreateReviewService createReviewService;

    public ReviewServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("리뷰 등록에 성공하였습니다.")
    void success_regiest_review() {
        Long placeId = 1L;
        User user = User.builder().userId(10L).nickname("TestUser_01").build();

        CreateReviewRequestDto request = new CreateReviewRequestDto("장소가 깨끗해요!", true, List.of("testUrl1.jpg", "testUrl2.jpg"), placeId);

        Place dummyPlace = Place.builder().id(placeId).placeName("testPlace").build();
        when(placeRepository.findById(placeId)).thenReturn(java.util.Optional.of(dummyPlace));

        Review target = Review.builder()
                .reviewId(100L)
                .place(dummyPlace)
                .user(user)
                .content(request.getContent())
                .isLove(request.getIsLove())
                .imageUrls(request.getImageUrls())
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(target);

        Long result = createReviewService.create(request, user.getUserId(), user.getNickname());

        assertThat(result).isEqualTo(100L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }
}
