package com.project.hiptour.common.usercase.place;

import com.project.hiptour.common.util.PageResponseDto;
import com.project.hiptour.common.web.place.PlaceDto;
import org.springframework.data.domain.Pageable;


public interface PlaceService {
    PlaceDto findPlace(Integer placeId);
    PageResponseDto<PlaceDto> findRecommendedPlaces(Pageable pageable);
}
