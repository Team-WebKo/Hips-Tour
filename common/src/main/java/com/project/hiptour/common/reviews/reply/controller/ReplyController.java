package com.project.hiptour.common.reviews.reply.controller;

import com.project.hiptour.common.reviews.reply.dto.ReplyRequestDto;
import com.project.hiptour.common.reviews.reply.dto.ReplyResponseDto;
import com.project.hiptour.common.reviews.reply.service.ReplyService;
import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews/{reviewId}/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<?> createReply(
            @PathVariable Long reviewId,
            @RequestBody ReplyRequestDto requestDto
            //사용자 인증 정보 필요
            ) {
        User user = new User();
        requestDto = bindReviewId(requestDto, reviewId);
        Long replyId = replyService.createReply(requestDto, user);
        return ResponseEntity.ok().body(replyId);
    }


    @GetMapping
    public ResponseEntity<List<ReplyResponseDto>> getReplies(
            @PathVariable Long reviewId
    ) {
        List<ReplyResponseDto> replies = replyService.getReplies(reviewId);
        return ResponseEntity.ok(replies);
    }

    private ReplyRequestDto bindReviewId(ReplyRequestDto dto, Long reviewId) {
        return new ReplyRequestDto(reviewId, dto.getContent());
    }
}
