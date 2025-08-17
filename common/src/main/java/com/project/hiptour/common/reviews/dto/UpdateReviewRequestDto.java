package com.project.hiptour.common.reviews.dto;

import com.project.hiptour.common.entity.review.HashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {
    private String headText;
    private String bodyText;
    private List<HashTag> hashTags;
}
