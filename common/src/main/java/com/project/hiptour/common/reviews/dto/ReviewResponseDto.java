package com.project.hiptour.common.reviews.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private Long placeId;
    private String content;
    private Boolean isLove;
    private List<String> imageUrls;
    private LocalDateTime createsAt;
    private LocalDateTime updatedAt;
    private WriterInfo writer;
}
