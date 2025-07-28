package com.project.hiptour.common.reviews.reply.repository;

import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByReviewOrderByCreatedAtAsc(Review review);
}
