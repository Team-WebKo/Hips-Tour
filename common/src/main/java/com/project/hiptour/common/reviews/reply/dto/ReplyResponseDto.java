package com.project.hiptour.common.reviews.reply.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyResponseDto {
    private Long replyId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
}
