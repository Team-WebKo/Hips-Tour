package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.ReviewSelections;
import com.project.hiptour.common.entity.perference.ReviewSelectionsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSelectionsRepository extends JpaRepository<ReviewSelections, ReviewSelectionsId> {
}
