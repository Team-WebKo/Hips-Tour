package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.entity.Review;
import com.project.hiptour.common.reviews.repository.PlaceRepository;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import com.project.hiptour.common.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    public Optional<Review> findByUserAndPlace(User user, Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new IllegalArgumentException("장소가 존재하지 않습니다."));

        return reviewRepository.findByUserAndPlace(user, place);
    }
}
