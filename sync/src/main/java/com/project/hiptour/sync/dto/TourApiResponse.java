package com.project.hiptour.sync.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TourApiResponse {
    private TourApiHeader header;
    private TourApiBody body;
}
