package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseTimeEntity;
import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.embedable.TelNumber;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Place extends BaseUpdateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    private String placeName;

    @Embedded
    private GeoPoint geoPoint;

    @Embedded
    private TelNumber telNumber;

    private String address1;
    private String address2;


    public Place(String placeName, String address1, String address2, GeoPoint geoPoint, TelNumber telNumber) {
        this.placeName = placeName;
        this.address1 = address1;
        this.address2 = address2;
        this.geoPoint = geoPoint;
        this.telNumber = telNumber;
    }
}
