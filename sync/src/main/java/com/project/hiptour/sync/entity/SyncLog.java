package com.project.hiptour.sync.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 중간 로그 저장 전략을 위한 Entity입니다.
 * **/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private int count;
    private LocalDateTime createdAt;

    public SyncLog(String status, int count, LocalDateTime createdAt) {
        this.status = status;
        this.count = count;
        this.createdAt = createdAt;
    }
}
