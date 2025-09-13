package com.project.hiptour.sync.infrastructure.persistence;

import com.project.hiptour.sync.domain.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncStatusRepository extends JpaRepository<SyncStatus, String> {
}
