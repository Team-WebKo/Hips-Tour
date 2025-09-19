package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.util.TimeProvider;
import com.project.hiptour.sync.domain.SyncJobType;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyJobService {
    private final SyncService syncService;
    private final OverviewFillService overviewFillService;
    private final SyncStatusRepository syncStatusRepository;
    private final TimeProvider timeProvider;

    @Value("${sync.load.daily-api-call-limit}")
    private int dailyApiCallLimit;

    public void runDailyTasks() {
        log.info("일일 동기화 및 데이터 보강 작업을 시작합니다.");

        Optional<LocalDateTime> lastSyncTimeOptional = getLastSyncTimeFromDB();
        if (lastSyncTimeOptional.isEmpty()) {
            log.warn("DB에 동기화 기준 시간이 없습니다. 적재 작업이 완료되어야 합니다. 작업을 생략합니다.");
            return;
        }

        LocalDateTime lastSyncTime = lastSyncTimeOptional.get();
        LocalDateTime syncStartedTime = timeProvider.now();
        log.info("마지막 동기화 시간: {}", lastSyncTime);

        try {
            log.info("변경분 동기화를 시작합니다.");
            int apiCallsUsedInSync = syncService.syncUpdatedPlaces(lastSyncTime);
            log.info("증분 동기화 완료. 사용돤 API 호출: {}", apiCallsUsedInSync);

            int remainingBudget = dailyApiCallLimit - apiCallsUsedInSync;
            log.info("Place-Overview 보강을 시작합니다. 사용 가능 API 호출: {}",remainingBudget);
            overviewFillService.fillMissingOverviews(remainingBudget);
            log.info("Place-Overview 보강 완료.");

            updateLastSyncTimeToDB(syncStartedTime);
            log.info("모든 일일 작업이 성공하였습니다.");

        } catch (Exception e) {
            log.error("일일 작업(동기화 or 보강) 중 오류가 발생했습니다. 동기화 시간은 업데이트되지 않습니다.", e);
        }
    }

    private Optional<LocalDateTime> getLastSyncTimeFromDB() {
        return syncStatusRepository.findById(SyncJobType.PLACE_SYNC)
                .map(SyncStatus::getLastSuccessTime);
    }

    private void updateLastSyncTimeToDB(LocalDateTime time) {
        SyncStatus status = new SyncStatus(SyncJobType.PLACE_SYNC, time);
        syncStatusRepository.save(status);
        log.info("동기화 성공 시간을 DB에 기록합니다. 마지막 성공 시간: {}", time);
    }
}
