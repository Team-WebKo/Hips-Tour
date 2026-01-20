package com.project.hiptour.common.entity.perference.repos;

import com.project.hiptour.common.entity.perference.OCEAN_Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OCEAN_MasterRepository extends JpaRepository<OCEAN_Master, Integer> {
}
