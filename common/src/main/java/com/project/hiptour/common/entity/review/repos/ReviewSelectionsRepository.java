package com.project.hiptour.common.entity.review.repos;

import com.project.hiptour.common.entity.perference.ReviewSelections;
import com.project.hiptour.common.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSelectionsRepository extends JpaRepository<ReviewSelections, Long> {
    List<ReviewSelections> findByReview(Review review);
}
