package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.UserPersonalityStat;
import com.project.hiptour.common.entity.users.UserPersonalityStatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersonalityStatRepository extends JpaRepository<UserPersonalityStat, UserPersonalityStatId> {
}
