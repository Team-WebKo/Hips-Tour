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

    public LogService(SyncLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailLog(String message, Exception e) {
        logRepository.save(new SyncLog(message, 0, LocalDateTime.now()));
    }
}
