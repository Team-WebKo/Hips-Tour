package com.project.hiptour.sync.infrastructure.persistence;

import com.project.hiptour.sync.domain.TourPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourPlaceRepository extends JpaRepository<TourPlace, String> {
}
