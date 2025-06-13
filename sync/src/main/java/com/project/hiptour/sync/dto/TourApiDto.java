package com.project.hiptour.sync.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TourApiDto {

    @JsonProperty("")
    private String placeId;

    @JsonProperty("title")
    private String placeName;

    @JsonProperty("addr1")
    private String address1;

    @JsonProperty("addr2")
    private String address2;

    @JsonProperty("mapx")
    private String latitude;

    @JsonProperty("mapy")
    private String longitude;

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getAddr1() {
        return address1;
    }

    public String getAddr2() {
        return address2;
    }

    public String getMapx() {
        return latitude;
    }

    public String getMapy() {
        return longitude;
    }
}
