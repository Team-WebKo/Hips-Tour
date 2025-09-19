package com.project.hiptour.sync.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * 설계 의도: syncId를 ID로 사용 > 추후 다른 정보의 동기화 기능에도 하나의 테이블에서 상태 관리 가능하다고 판단
 * **/
@Entity
@Table(name = "sync_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncStatus {
    @Id
    @Enumerated(EnumType.STRING)
    private SyncJobType syncId;

    private LocalDateTime lastSuccessTime;
}