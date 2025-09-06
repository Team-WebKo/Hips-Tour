package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.users.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    Optional<List<UserRole>> findByUserInfo(UserInfo userId);
}
