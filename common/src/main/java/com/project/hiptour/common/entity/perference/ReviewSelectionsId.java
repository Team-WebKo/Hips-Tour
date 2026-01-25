package com.project.hiptour.common.entity.perference;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReviewSelectionsId implements Serializable {
    private Long reviewId;
    private Integer keywordId;
}
