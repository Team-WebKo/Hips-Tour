package com.project.hiptour.sync.application;

import com.project.hiptour.sync.entity.SyncLog;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LogService {
    private final SyncLogRepository logRepository;
    private final AlarmService alarmService;

    public LogService(SyncLogRepository logRepository, AlarmService alarmService) {
        this.logRepository = logRepository;
        this.alarmService = alarmService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailLog(String syncType, Exception e) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastSuccessTime = logRepository
                .findTopBySyncTypeAndStatusOrderByLastSuccessSyncAtDesc(syncType,"SUCCESS")
                .map(SyncLog::getLastSuccessSyncAt)
                .orElse(now.minusDays(1));

        String message = e.getMessage() != null ? e.getMessage() : e.toString();

        SyncLog failLog = new SyncLog(syncType, "FAIL", 0, now, lastSuccessTime, message);
        logRepository.save(failLog);

        long recentFailCount = logRepository.countBySyncTypeAndStatusAndCreatedAtAfter(
                syncType, "FAIL", now.minusHours(23)
        );

        if (recentFailCount >= 5) {
            alarmService.notifyAdmin(
                    "[" + syncType + "] 동기화 연속 실패 발생",
                    "최근 23시간 동안 실패 " + recentFailCount + "회 발생"
            );
        }
    }
}
