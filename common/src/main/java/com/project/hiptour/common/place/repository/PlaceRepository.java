package com.project.hiptour.common.place.repository;

import com.project.hiptour.common.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
    List<Place> findByCategoryCategoryId(Integer categoryId);
}
