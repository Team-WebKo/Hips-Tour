package com.project.hiptour.common.reviews.repository;

import com.project.hiptour.common.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
