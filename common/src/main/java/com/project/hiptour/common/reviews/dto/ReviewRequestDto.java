package com.project.hiptour.common.reviews.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReviewRequestDto {
    @NotNull
    private Long placeId;

    @NotNull
    private String content;

    private Boolean isLove;

    private List<String> imageUrls;
}
