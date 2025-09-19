package com.project.hiptour.common.entity.place.embedable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class GeoPoint {
    private double latitude;
    private double longitude;
    private int mlevel;

    public GeoPoint(double latitude, double longitude, int mlevel) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mlevel = mlevel;
    }
}
