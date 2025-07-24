package com.project.hiptour.common.reviews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {
    @NotBlank(message = "변경된 내용이 없습니다.")
    private String content;

    private Boolean isLove;
    private List<String> imageUrls;
}
