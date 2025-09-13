package com.project.hiptour.common.application.reviews.config;

import com.project.hiptour.common.reviews.service.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockServiceConfig {

    @Bean
    public CreateReviewService createReviewService() {
        return Mockito.mock(CreateReviewService.class);
    }

    @Bean
    public ReviewService reviewService() {
        return Mockito.mock(ReviewService.class);
    }

    @Bean
    public DeleteReviewService deleteReviewService() {
        return Mockito.mock(DeleteReviewService.class);
    }

    @Bean
    public UpdateReviewService updateReviewService() {
        return Mockito.mock(UpdateReviewService.class);
    }

}
