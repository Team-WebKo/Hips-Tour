package com.project.hiptour.sync.infra.persistence;

import com.project.hiptour.sync.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository

public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {

    Optional<SyncLog> findTopBySyncTypeAndStatusOrderByLastSuccessSyncAtDesc(String syncType, String status);

    long countBySyncTypeAndStatusAndCreatedAtAfter(String syncType, String status, LocalDateTime after);
}
