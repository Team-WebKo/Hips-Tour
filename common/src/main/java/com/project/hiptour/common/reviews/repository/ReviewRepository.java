package com.project.hiptour.common.reviews.repository;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndPlace(User user, Place place);
}
