package com.project.hiptour.common.place.repository;

import com.project.hiptour.common.entity.place.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    @Query("SELECT p FROM Place p " +
            "LEFT JOIN Heart h ON p.placeId = h.feed.placeId AND h.isActive = true " +
            "GROUP BY p.placeId " +
            "ORDER BY COUNT(h) DESC")
    List<Place> findPlacesOrderByHeartCount(Pageable pageable);

    List<Place> findByPlaceIdNotInOrderByCreatedAtDesc(Collection<Integer> placeIds, Pageable pageable);

    List<Place> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
