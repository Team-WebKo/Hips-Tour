package com.project.hiptour.common.entity.perference;

import com.project.hiptour.common.entity.BaseUpdateEntity;
import com.project.hiptour.common.entity.place.Place;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "PlacePersonalityStat")
public class PlacePersonalityStat extends BaseUpdateEntity {
    @EmbeddedId
    private PlacePersonalityStatId id;

    @MapsId("placeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId")
    private Place place;

    @Column(name = "positiveCount", columnDefinition = "INT DEFAULT 0")
    private Integer positiveCount;

    @Column(name = "totalCount", columnDefinition = "INT DEFAULT 0")
    private Integer totalCount;

    @Column(name = "score", precision = 5, scale = 3, columnDefinition = "DECIMAL(5,3) DEFAULT 5.000")
    private BigDecimal score;

    @Builder
    public PlacePersonalityStat(Place place, DimensionType dimension, Integer positiveCount, Integer totalCount, BigDecimal score) {
        this.place = place;
        this.positiveCount = positiveCount;
        this.totalCount = totalCount;
        this.score = score;
        this.id = new PlacePersonalityStatId(place.getPlaceId(), dimension);
    }
}
