package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.Review_OCEAN_Selections;
import com.project.hiptour.common.entity.perference.Review_OCEAN_SelectionsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Review_OCEAN_SelectionsRepository extends JpaRepository<Review_OCEAN_Selections, Review_OCEAN_SelectionsId> {
}
