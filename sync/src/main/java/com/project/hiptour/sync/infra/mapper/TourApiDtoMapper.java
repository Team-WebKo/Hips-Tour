package com.project.hiptour.sync.infra.mapper;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.TourApiDto;
import org.springframework.stereotype.Component;

@Component
public class TourApiDtoMapper {

    public Place toEntity(TourApiDto dto) {
        return new Place(
                dto.getPlaceName(),
                dto.getAddress1(),
                dto.getAddress2(),
                dto.getLongitude(),
                dto.getLatitude()
        );
    }
}
