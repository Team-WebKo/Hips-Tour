package com.project.hiptour.common.reviews.repository;

import com.project.hiptour.common.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    boolean existsByPlaceNameAndAddress1(String placeName, String address1);
}
