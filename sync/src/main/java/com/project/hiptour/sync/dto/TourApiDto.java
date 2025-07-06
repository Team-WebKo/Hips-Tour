package com.project.hiptour.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TourApiDto {

    private String placeId;
    private String placeName;

    private String address1;
    private String address2;

    @JsonProperty("mapy")
    private double latitude;
    @JsonProperty("mapx")
    private double longitude;

    public TourApiDto(String title, String addr1, String addr2, double mapx, double mapy) {
        this.placeName = title;
        this.address1 = addr1;
        this.address2 = addr2;
        this.longitude = mapx;
        this.latitude = mapy;
    }
}
