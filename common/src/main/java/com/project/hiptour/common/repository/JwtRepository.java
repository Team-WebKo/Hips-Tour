package com.project.hiptour.common.repository;

import com.project.hiptour.common.domain.RefreshToken;
import com.project.hiptour.common.domain.UserTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRepository extends JpaRepository<RefreshToken, Long>  {

        boolean existsBykakaoId(Long kakaoid);

}