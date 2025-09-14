package com.project.hiptour.common.reviews.dto;

import com.project.hiptour.common.entity.review.HashTag;
import com.project.hiptour.common.entity.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MyReviewResponseDto {
    private Long reviewId;
    private Integer placeId;
    private String placeName;
    private String headText;
    private String bodyText;
    private List<HashTag> hashTags;
    private LocalDateTime createdAt;

    public MyReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.placeId = review.getPlace().getPlaceId();;
        this.placeName = review.getPlace().getPlaceName();
        this.headText = review.getHeadText();
        this.bodyText = review.getBodyText();
        this.hashTags = review.getHashTags();
        this.createdAt = review.getCreatedAt();
    }
}
