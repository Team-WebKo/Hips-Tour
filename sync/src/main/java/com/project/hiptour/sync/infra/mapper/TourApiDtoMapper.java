package com.project.hiptour.sync.infra.mapper;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.TourApiDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<Place> toEntity(List<TourApiDto> dtoList) {
        List<Place> places = new ArrayList<>();
        for(TourApiDto dto : dtoList) {
            Place place = toEntity(dto);
            places.add(place);
        }

        return places;
    }
}
