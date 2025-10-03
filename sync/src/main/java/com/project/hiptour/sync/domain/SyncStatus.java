package com.project.hiptour.sync.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sync_status")
@Getter
@Setter
@NoArgsConstructor
public class SyncStatus {
    @Id
    @Enumerated(EnumType.STRING)
    private SyncJobType syncId;

    @Enumerated(EnumType.STRING)
    private JobExecutionStatus status;

    private LocalDateTime lastSuccessTime;

    public SyncStatus(SyncJobType syncId) {
        this.syncId = syncId;
        this.status = JobExecutionStatus.FINISHED;
    }
}