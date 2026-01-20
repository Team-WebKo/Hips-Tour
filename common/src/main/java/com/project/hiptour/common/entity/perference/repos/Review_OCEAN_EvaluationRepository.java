package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.Review_OCEAN_Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Review_OCEAN_EvaluationRepository extends JpaRepository<Review_OCEAN_Evaluation, Long> {
}
