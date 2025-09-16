package com.project.hiptour.common.web.reviews;

import com.project.hiptour.common.entity.review.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateReviewRequestDto {
    private Long userId;
    private String headText;
    private String bodyText;
    private List<String> imageUrls;
    private List<HashTag> hashTags;
}
