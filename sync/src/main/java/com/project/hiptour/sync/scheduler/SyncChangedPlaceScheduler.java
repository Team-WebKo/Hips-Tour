package com.project.hiptour.sync.scheduler;

import com.project.hiptour.sync.application.SyncPlaceCommandHandler;
import com.project.hiptour.sync.dto.PlaceDto;
import com.project.hiptour.sync.entity.SyncLog;
import com.project.hiptour.sync.infra.api.TourApiCaller;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SyncChangedPlaceScheduler {
    private final SyncLogRepository syncLogRepository;
    private final TourApiCaller tourApiCaller;
    private final SyncPlaceCommandHandler syncPlaceCommandHandler;

    private static final String SYNC_TYPE = "PLACE";

    @Scheduled(cron = "${sync.schedule.place-cron}")
    public void syncChangedPlaces() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastSuccessTime = syncLogRepository
                .findTopBySyncTypeAndStatusOrderByLastSuccessSyncAtDesc(SYNC_TYPE, "SUCCESS")
                .map(SyncLog::getLastSuccessSyncAt)
                .orElse(now.minusDays(1));

        try {
            List<PlaceDto> updatedPlaces = tourApiCaller.fetchChangedSince(lastSuccessTime);
            syncPlaceCommandHandler.handle(updatedPlaces);

            SyncLog log = SyncLog.success(SYNC_TYPE, updatedPlaces.size(), now);
            syncLogRepository.save(log);

        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : e.toString();

            SyncLog failLog = new SyncLog(SYNC_TYPE, "FAIL", 0, now, lastSuccessTime, message);
            syncLogRepository.save(failLog);
        }
    }
}