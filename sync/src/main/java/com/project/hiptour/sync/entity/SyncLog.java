package com.project.hiptour.sync.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 중간 로그 저장 전략을 위한 Entity입니다.
 * **/
@Entity
@Getter
public class SyncLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String syncType;
    private String status;
    private int count;
    private LocalDateTime createdAt;

    private LocalDateTime lastSuccessSyncAt;

    protected SyncLog() {

    }

    public SyncLog(String syncType, String status, int count, LocalDateTime createdAt, LocalDateTime lastSuccessSyncAt) {
        this.syncType = syncType;
        this.status = status;
        this.count = count;
        this.createdAt = createdAt;
        this.lastSuccessSyncAt = lastSuccessSyncAt;
    }

    public static SyncLog success(String syncType, int count, LocalDateTime now) {
        return new SyncLog(syncType, "SUCCESS", count, now, now);
    }

    public static SyncLog fail(String syncType,  LocalDateTime now) {
        return new SyncLog(syncType, "FAIL", 0, now, null);
    }
}
