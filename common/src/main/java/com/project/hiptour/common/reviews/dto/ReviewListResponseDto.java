package com.project.hiptour.common.reviews.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListResponseDto {
    private Long reviewId;
    private String headText;
    private String bodyText;
//    private Boolean isLove;
    private List<String> imageUrls;
    private Long userId;
    private String nickname;
    private Boolean pinned;

}
