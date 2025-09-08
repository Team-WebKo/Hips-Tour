package com.project.hiptour.common.entity.heart.repos;

import com.project.hiptour.common.entity.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepos extends JpaRepository<Heart,Long> {
    Heart findByUserIdAndFeedId(long userId, int feedId);
}
