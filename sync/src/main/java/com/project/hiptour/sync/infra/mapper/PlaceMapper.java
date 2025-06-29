package com.project.hiptour.sync.infra.mapper;

import com.project.hiptour.common.place.GeoPoint;
import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.PlaceDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceMapper {
    public Place toEntity(PlaceDto dto) {
        return new Place(
                dto.getName(),
                dto.getAddress1(),
                dto.getAddress2(),
                new GeoPoint(dto.getLatitude(), dto.getLongitude())
        );
    }

    public List<Place> toEntity(List<PlaceDto> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .toList();
    }
}