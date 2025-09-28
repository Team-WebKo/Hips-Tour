package com.project.hiptour.sync.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SyncJobType {
    PLACE_SYNC("placeSync");

    private final String jobId;
}
