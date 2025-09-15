package com.project.hiptour.common.reviews.dto;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDto extends BaseTimeEntity {
    private Long reviewId;
    private String headText;
    private String bodyText;
    private List<String> imageUrils;
    private Long userId;
    private String nickname;

    public static ReviewListResponseDto from(Review review) {
        ReviewListResponseDto dto = ReviewListResponseDto.builder()
                .reviewId(review.getReviewId())
                .headText(review.getHeadText())
                .bodyText(review.getBodyText())
                .imageUrils(review.getImageUrls())
                .userId(review.getUserInfo().getUserId())
                .nickname(review.getUserInfo().getNickName())
                .build();
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        return dto;
    }
}