package com.project.hiptour.common.web.reviews;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.review.HashTag;
import com.project.hiptour.common.entity.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewResponseDto extends BaseTimeEntity {
    private Long reviewId;
    private Integer placeId;
    private String placeName;
    private String headText;
    private String bodyText;
    private List<String> imageUrls;
    private List<String> hashTags;

    public static MyReviewResponseDto from(Review review) {
        MyReviewResponseDto dto = MyReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .placeId(review.getPlace().getPlaceId())
                .placeName(review.getPlace().getPlaceName())
                .headText(review.getHeadText())
                .bodyText(review.getBodyText())
                .imageUrls(review.getImageUrls())
                .hashTags(review.getHashTags().stream()
                        .map(HashTag::getOriginal)
                        .collect(Collectors.toList()))
                .build();
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        return dto;
    }
}
