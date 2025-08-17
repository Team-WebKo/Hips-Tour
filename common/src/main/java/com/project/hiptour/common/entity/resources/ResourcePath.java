package com.project.hiptour.common.entity.resources;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ResourcePath {
    private String path;
}
