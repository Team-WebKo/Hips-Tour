package com.project.hiptour.common.entity.review.repos;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.UserInfo;
import com.project.hiptour.common.entity.review.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserInfoAndPlace(UserInfo userInfo, Place place);

    List<Review> findByUserInfoOrderByCreatedAtDesc(UserInfo userInfo);

    // 무한스크롤이라 한번에 다받으면 과부화 올까봐 JPA Pageable로 처리
    @Query("SELECT r FROM Review r " +
            "WHERE r.place.id = :placeId " +
            "ORDER BY " +
            "CASE WHEN r.pinned = true THEN 0 ELSE 1 END, " +
            "r.pinnedAt DESC NULLS LAST, " +
            "r.createdAt DESC")
    List<Review> findByPlaceIdOrderedWithOffsetLimit(
            @Param("placeId") int placeId,
            Pageable pageable
    );

    default List<Review> findByPlaceIdWithOffsetLimit(int placeId, int offset, int limit) {
        return findByPlaceIdOrderedWithOffsetLimit(
                placeId,
                PageRequest.of(offset / limit, limit, Sort.unsorted())
        );
    }
}