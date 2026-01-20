package com.project.hiptour.common.entity.users;

import com.project.hiptour.common.entity.perference.DimensionType;
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
public class UserPersonalityStatId implements Serializable {
    private Long userId;

    @Enumerated(EnumType.STRING)
    private DimensionType dimension;
}
