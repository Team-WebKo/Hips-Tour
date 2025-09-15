package com.project.hiptour.common.reviews.dto;

import com.project.hiptour.common.entity.review.HashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {
    private Long userId;
    private String headText;
    private String bodyText;
    private List<String> imageUrls;
    private List<HashTag> hashTags;
}
