package com.project.hiptour.common.entity.place.repos;

import com.project.hiptour.common.entity.place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    boolean existsByPlaceNameAndAddress1(String placeName, String address1);

    @Query(value = "SELECT p FROM Place p " +
            "LEFT JOIN Heart h ON p.placeId = h.feed.placeId AND h.isActive = true " +
            "GROUP BY p.placeId " +
            "ORDER BY COUNT(h) DESC", countQuery = "SELECT count(p) FROM Place p LEFT JOIN Heart h ON p.placeId = h.feed.placeId AND h.isActive = true GROUP BY p.placeId")
    Page<Place> findPlacesOrderByHeartCount(Pageable pageable);

    Page<Place> findByPlaceIdNotInOrderByCreatedAtDesc(Collection<Integer> placeIds, Pageable pageable);

    Page<Place> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
