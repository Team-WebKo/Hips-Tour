package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.OceanMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OceanMasterRepository extends JpaRepository<OceanMaster, Integer> {
}
