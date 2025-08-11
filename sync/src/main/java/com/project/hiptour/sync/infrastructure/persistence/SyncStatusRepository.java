package com.project.hiptour.sync.infrastructure.persistence;

import com.project.hiptour.sync.domain.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncStatusRepository extends JpaRepository<SyncStatus, String> {
}
