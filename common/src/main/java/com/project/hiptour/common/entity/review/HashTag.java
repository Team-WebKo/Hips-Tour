package com.project.hiptour.common.entity.review;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class HashTag {
    private String original;
}
