package com.project.hiptour.common.place.serviceImpl;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.service.PlaceService;
import com.project.hiptour.common.reviews.global.exception.PlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    public PlaceDto findPlace(Integer placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("장소를 찾지 못했습니다: " + placeId));

        return PlaceDto.from(place);
    }

    @Override
    public Page<PlaceDto> findRecommendedPlaces(Pageable pageable) {
        Page<Place> likedPlacesPage = placeRepository.findPlacesOrderByHeartCount(pageable);

        if (likedPlacesPage.isEmpty() && pageable.getPageNumber() == 0) {
            return placeRepository.findAllByOrderByCreatedAtDesc(pageable).map(PlaceDto::from);
        }

        return likedPlacesPage.map(PlaceDto::from);
    }
}
