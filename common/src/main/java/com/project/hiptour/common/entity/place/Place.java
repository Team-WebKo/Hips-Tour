package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Place extends BaseUpdateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placeId;

    @Column(unique = true)
    private String contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentTypeId;

    private LocalDateTime sourceModifiedTime;

    private String placeName;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Embedded
    private GeoPoint geoPoint;

    private String telNumber;

    @Enumerated(EnumType.STRING)
    private AreaCode areaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionInfo regionInfo;

    private String address1;
    private String address2;

    @Builder
    public Place(String placeName, String imageUrl, String overview, GeoPoint geoPoint, String telNumber, RegionInfo regionInfo, String address1, String address2) {
        this.placeName = placeName;
        this.imageUrl = imageUrl;
        this.overview = overview;
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
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.geoPoint = geoPoint;
        this.telNumber = telNumber;
    }
}
