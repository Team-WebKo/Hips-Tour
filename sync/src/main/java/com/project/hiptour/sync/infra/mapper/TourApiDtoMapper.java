package com.project.hiptour.sync.infra.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.dto.TourApiResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TourApiDtoMapper {
    private final ObjectMapper objectMapper;

    public TourApiDtoMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Place toEntity(TourApiDto dto) {
        return new Place(
                dto.getPlaceName(),
                dto.getAddress1(),
                dto.getAddress2(),
                new GeoPoint(dto.getLatitude(), dto.getLongitude()),
                null
        );
    }

    public TourApiDto toDto(TourApiItem item) {
        return new TourApiDto(
                item.getTitle(),
                item.getAddr1(),
                item.getAddr2(),
                Double.parseDouble(item.getMapx()),
                Double.parseDouble(item.getMapy())
        );
    }

    public List<TourApiDto> toDtoList(List<TourApiItem> items) {
        List<TourApiDto> dtoList = new ArrayList<>();
        for (TourApiItem item : items) {
            dtoList.add(toDto(item));
        }
        return dtoList;
    }

    public List<Place> toEntity(List<TourApiDto> dtoList) {
        List<Place> places = new ArrayList<>();
        for(TourApiDto dto : dtoList) {
            Place place = toEntity(dto);
            places.add(place);
        }

        return places;
    }

    public List<TourApiItem> toItemList(String jsonResponse) {
        try {
            TourApiResponseDto response = objectMapper.readValue(jsonResponse, TourApiResponseDto.class);
            return response.getResponse()
                    .getBody()
                    .getItems()
                    .getItem();
        } catch (Exception e) {
            throw new RuntimeException("TourAPI 응답 파싱 실패", e);
        }
    }
}
