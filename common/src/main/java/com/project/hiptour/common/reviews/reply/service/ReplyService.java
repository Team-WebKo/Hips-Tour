package com.project.hiptour.common.reviews.reply.service;

import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.reply.dto.ReplyRequestDto;
import com.project.hiptour.common.reviews.reply.dto.ReplyResponseDto;
import com.project.hiptour.common.reviews.reply.entity.Reply;
import com.project.hiptour.common.reviews.reply.repository.ReplyRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;

    public Long createReply(ReplyRequestDto requestDto, User user) {
        Review review = reviewRepository.findById(requestDto.getReviewId()).orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        Reply reply = Reply.builder()
                .review(review)
                .user(user)
                .content(requestDto.getContent())
                .build();

        return replyRepository.save(reply).getReplyId();
    }


    public List<ReplyResponseDto> getReplies(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        return replyRepository.findByReviewOrderByCreatedAtAsc(review).stream()
                .map(reply -> ReplyResponseDto.builder()
                        .replyId(reply.getReplyId())
                        .content(reply.getContent())
                        .nickname(reply.getUser().getNickname())
                        .createdAt(reply.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
