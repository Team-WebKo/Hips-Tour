package com.project.hiptour.sync.external.api;

import com.project.hiptour.sync.dto.PlaceDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TourDataApiCaller {
    String fetchPlaceData(int areaCode);

    List<PlaceDto> fetchChangedSince(LocalDateTime lastSuccessTime);
}
