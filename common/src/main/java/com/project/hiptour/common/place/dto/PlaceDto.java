package com.project.hiptour.common.place.dto;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.embedable.TelNumber;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDto {
    private final Integer placeId;
    private final String placeName;
    private final String address1;
    private final String address2;
    private final GeoPoint geoPoint;
    private TelNumber telNumber;

    public static PlaceDto from(Place entity) {
        return PlaceDto.builder()
                .placeId(entity.getPlaceId())
                .placeName(entity.getPlaceName())
                .address1(entity.getAddress1())
                .address2(entity.getAddress2())
                .geoPoint(entity.getGeoPoint())
                .telNumber(entity.getTelNumber())
                .build();
    }
}
