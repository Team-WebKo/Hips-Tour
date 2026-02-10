package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserPersonalityStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPersonalityStatRepository extends JpaRepository<UserPersonalityStat, Long> {
    Optional<UserPersonalityStat> findByUserInfo(UserInfo userInfo);
}
