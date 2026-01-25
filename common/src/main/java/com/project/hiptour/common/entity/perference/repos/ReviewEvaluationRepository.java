package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.ReviewEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewEvaluationRepository extends JpaRepository<ReviewEvaluation, Long> {
}
