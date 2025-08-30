package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepos extends JpaRepository<UserInfo, Long> {
    boolean existsByUserIdentifier(String userIdentifier);
    Optional<UserInfo> findByUserIdentifier(String userIdentifier);

}
