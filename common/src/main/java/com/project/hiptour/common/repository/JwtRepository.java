package com.project.hiptour.common.repository;

import com.project.hiptour.common.domain.RefreshToken;
import com.project.hiptour.common.domain.UserTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<RefreshToken, Long>  {

        boolean existsByKakaoId(Long kakaoid);

        Optional<RefreshToken> findTopByKakaoIdOrderByExpireatDesc(Long kakaoId);

}