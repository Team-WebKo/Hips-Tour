package com.project.hiptour.common.entity.place.repos;

import com.project.hiptour.common.entity.place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Place, Integer> {
    Page<Place> findByPlaceNameContaining(String keyword, Pageable pageable);
}
