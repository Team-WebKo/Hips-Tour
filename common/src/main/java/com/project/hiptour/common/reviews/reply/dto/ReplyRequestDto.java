package com.project.hiptour.common.reviews.reply.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {
    private Long reviewId;
    private String content;
}
