package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
