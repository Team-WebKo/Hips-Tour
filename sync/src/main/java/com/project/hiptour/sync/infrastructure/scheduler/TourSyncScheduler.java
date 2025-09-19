package com.project.hiptour.sync.infrastructure.scheduler;

import com.project.hiptour.sync.application.service.OverviewFillService;
import com.project.hiptour.sync.application.service.SyncService;
import com.project.hiptour.sync.application.util.TimeProvider;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourSyncScheduler {
    private final SyncService syncService;
    private final OverviewFillService overviewFillService;
    private final SyncStatusRepository syncStatusRepository;
    private final TimeProvider timeProvider;

    @Value("${sync.load.daily-api-call-limit}")
    private int dailyApiCalLimit;

    private static final String SYNC_ID = "placeSync";

    @Scheduled(cron = "${sync.schedule.cron}")
    @Transactional
    public void runScheduledTasks() {
        log.info("정기 TourAPI 데이터 동기화 스케쥴을 시작합니다.");

        Optional<LocalDateTime> lastSyncTimeOptional = getLastSyncTimeFromDB();
        if (lastSyncTimeOptional.isEmpty()) {
            log.warn("DB에 동기화 기준 시간이 없습니다. 적재 작업이 완료되어야 합니다. 동기화 작업을 건너뜁니다.");
            return;
        }

        LocalDateTime lastSyncTime = lastSyncTimeOptional.get();
        LocalDateTime syncStartedTime = timeProvider.now();
        log.info("마지막 동기화 시간: {}", lastSyncTime);

        try {
            log.info("변경분 동기화를 시작힙니다.");
            int apiCallsUserdInSync = syncService.syncUpdatedPlaces(lastSyncTime);
            log.info("TourAPI 변경분 동기화가 완료되었습니다. 사용된 API 호출: {}", apiCallsUserdInSync);

            int remainingBudget = dailyApiCalLimit - apiCallsUserdInSync;
            log.info("상세 정보(Overview) 추가 작업을 시작합니다. 사용 가능 API 호출: {}", remainingBudget);
            overviewFillService.fillMissingOverviews(remainingBudget);
            log.info("상세 정보(Overview) 추가 작업을 완료했습니다.");

            updateLastSyncTimeToDB(syncStartedTime);
            log.info("모든 동기화 및 개요 추가 작업이 성공적으로 완료되었습니다.");

        } catch (Exception e) {
            log.error("스케줄 작업(동기화 or 개요 추가) 중 오류가 발생했습니다. 동기화 시간은 업데이트되지 않습니다.", e);
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