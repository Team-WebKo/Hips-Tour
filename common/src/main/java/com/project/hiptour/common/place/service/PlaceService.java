package com.project.hiptour.common.place.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceDto findPlace(Integer id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("장소를 찾지 못했습니다: " + id));
        return PlaceDto.from(place);
    }

    public List<PlaceDto> findRecommendedPlaces(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Place> likedPlaces = placeRepository.findPlacesOrderByHeartCount(pageable);

        if (likedPlaces.size() < limit) {
            int remainingLimit = limit - likedPlaces.size();
            Pageable remainingPageable = PageRequest.of(0, remainingLimit);

            List<Place> recentPlaces;
            if (likedPlaces.isEmpty()) {
                recentPlaces = placeRepository.findAllByOrderByCreatedAtDesc(remainingPageable);
            } else {
                Collection<Integer> excludeIds = likedPlaces.stream()
                        .map(Place::getPlaceId)
                        .collect(Collectors.toList());
                recentPlaces = placeRepository.findByPlaceIdNotInOrderByCreatedAtDesc(excludeIds, remainingPageable);
            }

            likedPlaces.addAll(recentPlaces);
        }

        return likedPlaces.stream()
                .map(PlaceDto::from)
                .collect(Collectors.toList());
    }
}
