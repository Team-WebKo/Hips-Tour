package com.project.hiptour.sync.infrastructure.scheduler;

import com.project.hiptour.sync.application.service.SyncService;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourSyncScheduler {
    private final SyncService syncService;
    private final SyncStatusRepository syncStatusRepository;
    private static final String SYNC_ID = "placeSync";

    @Scheduled(cron = "${sync.schedule.cron}")
    public void runSync() {
        log.info("정기 TourAPI 데이터 동기화 스케쥴을 시작합니다.");

        Optional<LocalDateTime> lastSyncTimeOptional = getLastSyncTimeFromDB();
        if (lastSyncTimeOptional.isEmpty()) {
            log.warn("DB에 동기화 기준 시간이 없습니다. 적재 작업이 완료되어야 합니다. 동기화 작업을 건너뜁니다.");
            return;
        }

        LocalDateTime lastSyncTime = lastSyncTimeOptional.get();
        LocalDateTime syncStartedTime = LocalDateTime.now();
        log.info("마지막 동기화 시간: {}", lastSyncTime);

        try {
            syncService.syncUpdatedPlaces(lastSyncTime);
            updateLastSyncTimeToDB(syncStartedTime);
            log.info("TourAPI 변경분 동기화가 완료되었습니다.");
        } catch (Exception e) {
            log.error("TourAPI 변경분 동기화 중 오류가 발생했습니다.", e);
        }

        log.info("정기 TourAPI 데이터 동기화 스케줄을 종료합니다.");
    }

    private Optional<LocalDateTime> getLastSyncTimeFromDB() {
        return syncStatusRepository.findById(SYNC_ID)
                .map(SyncStatus::getLastSuccessTime);
    }

    private void updateLastSyncTimeToDB(LocalDateTime time) {
        SyncStatus status = new SyncStatus(SYNC_ID, time);
        syncStatusRepository.save(status);
        log.info("동기화 성공 시간을 DB에 기록했습니다. 마지막 성공 시간: {}", time);
    }
}