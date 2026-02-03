package com.project.hiptour.common.entity.place.repos;

import com.project.hiptour.common.entity.place.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Integer> {
}
