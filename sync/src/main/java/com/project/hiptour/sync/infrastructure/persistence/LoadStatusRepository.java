package com.project.hiptour.sync.infrastructure.persistence;

import com.project.hiptour.sync.domain.LoadJobStatus;
import com.project.hiptour.sync.domain.LoadJobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadStatusRepository extends JpaRepository<LoadJobStatus, LoadJobType> {
}
