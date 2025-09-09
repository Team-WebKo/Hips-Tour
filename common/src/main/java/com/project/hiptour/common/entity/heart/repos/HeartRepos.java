package com.project.hiptour.common.entity.heart.repos;

import com.project.hiptour.common.entity.heart.Heart;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartRepos extends JpaRepository<Heart,Long> {
    List<Heart> findByUserUserIdAndFeedPlaceId(Long userId, int feedId);
}
