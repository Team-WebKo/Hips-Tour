package com.project.hiptour.common.place.service;

import com.project.hiptour.common.place.dto.PlaceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PlaceService {
    PlaceDto findPlace(Integer placeId);
    Page<PlaceDto> findRecommendedPlaces(Pageable pageable);
}
