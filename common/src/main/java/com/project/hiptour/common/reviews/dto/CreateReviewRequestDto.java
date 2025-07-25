package com.project.hiptour.common.reviews.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {
    @NotNull(message = "장소 ID는 필수항목입니다.")
    private Long placeId;

    @NotNull(message = "내용을 입력해 주세요.")
    private String content;

    private Boolean isLove;

    private List<String> imageUrls;
}
