package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.PlacePersonalityStat;
import com.project.hiptour.common.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacePersonalityStatRepository extends JpaRepository<PlacePersonalityStat, Long> {
    Optional<PlacePersonalityStat> findByPlace(Place place);
}
