package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Place extends BaseUpdateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placeId;

    @Column(unique = true)
    private String contentId;

    private String placeName;

    @Embedded
    private GeoPoint geoPoint;

    private String telNumber;
    private String areaCode;

    @OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionInfo regionInfo;

    private String address1;
    private String address2;

    @Builder
    public Place(String placeName, GeoPoint geoPoint, String telNumber, RegionInfo regionInfo, String address1, String address2) {
        this.placeName = placeName;
        this.geoPoint = geoPoint;
        this.telNumber = telNumber;
        this.regionInfo = regionInfo;
        this.address1 = address1;
        this.address2 = address2;
    }

    public Place(String placeName, String address1, String address2, GeoPoint geoPoint, String telNumber) {
        this.placeName = placeName;
        this.address1 = address1;
        this.address2 = address2;
        this.geoPoint = geoPoint;
        this.telNumber = telNumber;
    }
}
