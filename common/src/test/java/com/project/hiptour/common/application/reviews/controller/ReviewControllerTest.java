package com.project.hiptour.common.application.reviews.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.application.reviews.config.MockServiceConfig;
import com.project.hiptour.common.reviews.controller.ReviewController;
import com.project.hiptour.common.reviews.dto.ReviewListResponseDto;
import com.project.hiptour.common.reviews.dto.ReviewPinRequestDto;
import com.project.hiptour.common.reviews.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
@Import(MockServiceConfig.class) // 공용 Mock 설정 불러오기
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreateReviewService createReviewService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private DeleteReviewService deleteReviewService;

    @Autowired
    private UpdateReviewService updateReviewService;

    @Autowired
    private ReviewQueryService reviewQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("특정 장소 리뷰 목록 조회 성공")
    void getReviewsByPlaceIdSuccess() throws Exception {
        Long placeId = 1L;
        int offset = 0;
        int limit = 10;

        ReviewListResponseDto review = new ReviewListResponseDto(
                1L,
                "내용1",
                true,
                List.of("https://image.com/1", "https://image.com/2"),
                100L,
                "hip_user",
                true
        );

        Mockito.when(reviewService.getReviewsByPlaceId(eq(placeId), eq(offset), eq(limit)))
                .thenReturn(List.of(review));

        mockMvc.perform(get("/api/place/{placeId}", placeId)
                        .param("offset", String.valueOf(offset))
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(1L))
                .andExpect(jsonPath("$[0].content").value("내용1"))
                .andExpect(jsonPath("$[0].isLove").value(true))
                .andExpect(jsonPath("$[0].imageUrls[0]").value("https://image.com/1"))
                .andExpect(jsonPath("$[0].userId").value(100L))
                .andExpect(jsonPath("$[0].nickname").value("hip_user"))
                .andExpect(jsonPath("$[0].pinned").value(true));
    }

    @Test
    @DisplayName("리뷰 핀 수정 성공 (204 No Content)")
    void pinReviewSuccess() throws Exception {
        Long reviewId = 1L;
        ReviewPinRequestDto requestDto = new ReviewPinRequestDto(true);

        mockMvc.perform(patch("/api/{reviewId}/pin", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNoContent());
    }
}