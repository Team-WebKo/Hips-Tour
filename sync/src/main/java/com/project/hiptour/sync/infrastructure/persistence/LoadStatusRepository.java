package com.project.hiptour.sync.infrastructure.persistence;

import com.project.hiptour.sync.domain.LoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadStatusRepository extends JpaRepository<LoadStatus, String> {
}
