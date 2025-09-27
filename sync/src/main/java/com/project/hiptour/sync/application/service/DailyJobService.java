package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.util.TimeProvider;
import com.project.hiptour.sync.domain.JobExecutionStatus;
import com.project.hiptour.sync.domain.SyncJobType;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional
    public void runDailyTasks() {
        log.info("일일 동기화 및 데이터 보강 작업을 시작합니다.");

        SyncStatus status = syncStatusRepository.findById(SyncJobType.PLACE_SYNC)
                .orElseGet(() -> new SyncStatus(SyncJobType.PLACE_SYNC));

        if (status.getStatus() == JobExecutionStatus.RUNNING) {
            log.warn("이전 동기화 작업이 아직 실행 중입니다. 이번 작업을 건너뜁니다.");
            return;
        }

        status.setStatus(JobExecutionStatus.RUNNING);
        syncStatusRepository.save(status);

        LocalDateTime lastSyncTime = status.getLastSuccessTime();
        LocalDateTime syncStartedTime = timeProvider.now();
        log.info("마지막 동기화 시간: {}", lastSyncTime);

        try {
            log.info("변경분 동기화를 시작합니다.");
            int apiCallsUsedInSync = syncService.syncUpdatedPlaces(lastSyncTime);
            log.info("증분 동기화 완료. 사용돤 API 호출: {}", apiCallsUsedInSync);

            int remainingBudget = dailyApiCallLimit - apiCallsUsedInSync;
            if (remainingBudget > 0) {
                log.info("Place-Overview 보강 작업을 시작합니다. 사용 가능 API 호출: {}", remainingBudget);
                overviewFillService.fillMissingOverviews(remainingBudget);
                log.info("Place-Overview 보강 완료.");
            }

            status.setStatus(JobExecutionStatus.FINISHED);
            status.setLastSuccessTime(syncStartedTime);
            log.info("모든 일일 작업이 완료되었습니다.");

        } catch (Exception e) {
            log.error("일일 작업(동기화 or 보강) 중 오류가 발생했습니다. 동기화 시간은 업데이트되지 않습니다.", e);
            status.setStatus(JobExecutionStatus.ERROR);
        } finally {
            syncStatusRepository.save(status);
        }
    }
}
