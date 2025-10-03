package com.project.hiptour.sync.infrastructure.scheduler;

import com.project.hiptour.sync.application.service.DailyJobService;
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
    private final DailyJobService dailyJobService;

    @Scheduled(cron = "${sync.schedule.crom}")
    public void runScheduledTasks() {
        log.info("정기 스케줄러가 DailyJobService를 실행합니다.");
        dailyJobService.runDailyTasks();
        log.info("정기 스케줄러 작업 호출이 완료되었습니다.");
    }
}