package com.project.hiptour.common.repository;

import com.project.hiptour.common.domain.UserTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestRepository extends JpaRepository<UserTest, Long> {

    boolean existsByOauthName(Long kakaoid);

}
