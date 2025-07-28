package com.project.hiptour.common.place;

import com.project.hiptour.common.reviews.entity.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class Place {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

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