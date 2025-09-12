package com.project.hiptour.common.entity.place;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.embedable.TelNumber;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place extends BaseUpdateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer placeId;

    @Column(unique = true)
    private String contentId;

    private String placeName;

    @Embedded
    private GeoPoint geoPoint;

    @Embedded
    private TelNumber telNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionInfo regionInfo;

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
