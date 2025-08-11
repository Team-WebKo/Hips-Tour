package com.project.hiptour.sync.application.port;

import java.time.LocalDateTime;

public interface TourApiPort {
    String fetchPlaceData(int pageNo, int numOfRows);

    String fetchChangedPlaces(LocalDateTime lastSyncTime, int pageNo, int numOfRows);
}
