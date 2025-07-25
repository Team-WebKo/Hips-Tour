package com.project.hiptour.common.reviews.repository;

import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.place.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.project.hiptour.common.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  
  Optional<Review> findByUserAndPlace(User user, Place place);

    //무한스크롤이라 한번에 다받으면 과부화 올까봐  JPA Pageable로 처리
    @Query("SELECT r FROM Review r WHERE r.placeId = :placeId ORDER BY r.createdAt DESC")
    List<Review> findByPlaceIdWithOffsetLimit(
            @Param("placeId") Long placeId,
            Pageable pageable
    );

    default List<Review> findByPlaceIdWithOffsetLimit(Long placeId, int offset, int limit) {
        return findByPlaceIdWithOffsetLimit(placeId, PageRequest.of(offset / limit, limit));
    }
}