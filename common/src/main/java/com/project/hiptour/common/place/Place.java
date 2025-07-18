package com.project.hiptour.common.place;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Place {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;

    @Embedded
    private GeoPoint geoPoint;

    private String address1;
    private String address2;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Place(String placeName, String address1, String address2, GeoPoint geoPoint) {
        this.placeName = placeName;
        this.address1 = address1;
        this.address2 = address2;
        this.geoPoint = geoPoint;
    }
}