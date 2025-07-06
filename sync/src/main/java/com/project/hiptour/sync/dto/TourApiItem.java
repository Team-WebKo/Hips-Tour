package com.project.hiptour.sync.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiItem {
    private String title;
    private String addr1;
    private String addr2;
    private String mapx;
    private String mapy;

    public TourApiDto toDto() {
        return new TourApiDto(title, addr1, addr2, parseDouble(mapx), parseDouble(mapy));
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
