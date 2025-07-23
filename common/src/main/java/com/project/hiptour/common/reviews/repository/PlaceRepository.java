package com.project.hiptour.common.reviews.repository;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByPlaceNameAndAddress1(String placeName, String address1);

}
