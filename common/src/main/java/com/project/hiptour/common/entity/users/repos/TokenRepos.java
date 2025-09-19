package com.project.hiptour.common.entity.users.repos;

import com.project.hiptour.common.entity.users.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepos extends JpaRepository<TokenInfo, Long> {
    Optional<TokenInfo> findLastByUserIdOrderByCreatedAtDesc(Long userId);

}
