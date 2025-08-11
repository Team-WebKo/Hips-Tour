package com.project.hiptour.sync.infrastructure.scheduler;

import com.project.hiptour.sync.application.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourSyncScheduler {
    private final SyncService syncService;

    @Scheduled(cron = "${sync.schedule.cron}")
    public void runSync() {
        log.info("정기 TourAPI 데이터 동기화 스케쥴을 시작합니다.");
        syncService.syncUpdatedPlaces();
        log.info("정기 TourAPI 데이터 동기화 스케줄을 종료합니다.");
    }
}
