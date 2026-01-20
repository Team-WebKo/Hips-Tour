package com.project.hiptour.common.entity.perference;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlacePersonalityStatId implements Serializable {
    private Integer placeId;

    @Enumerated(EnumType.STRING)
    private DimensionType dimension;
}
