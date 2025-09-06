package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleInfoRepo extends JpaRepository<RoleInfo, Integer> {
}
