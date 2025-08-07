package com.project.hiptour.sync.infra.persistence;

import com.project.hiptour.common.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceSyncRepository extends JpaRepository<Place, Long> {

}
