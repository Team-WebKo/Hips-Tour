package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.PlacePersonalityStat;
import com.project.hiptour.common.entity.perference.PlacePersonalityStatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacePersonalityStatRepository extends JpaRepository<PlacePersonalityStat, PlacePersonalityStatId> {
}
