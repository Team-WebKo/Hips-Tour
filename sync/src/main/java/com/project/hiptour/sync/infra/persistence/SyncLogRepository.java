package com.project.hiptour.sync.infra.persistence;

import com.project.hiptour.sync.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {

}
