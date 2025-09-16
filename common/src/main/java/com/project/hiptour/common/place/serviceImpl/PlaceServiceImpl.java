package com.project.hiptour.common.place.serviceImpl;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.service.PlaceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    public PlaceDto findPlace(Integer placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("장소를 찾지 못했습니다: " + placeId));

        return PlaceDto.from(place);
    }

    @Override
    public Page<PlaceDto> findRecommendedPlaces(Pageable pageable) {
        Page<Place> heartedPlacesPage = placeRepository.findPlacesOrderByHeartCount(pageable);

        if (heartedPlacesPage.getContent().size() < pageable.getPageSize() || heartedPlacesPage.isEmpty()) {
            List<Place> heartedPlacesOrSortedPlaces = new ArrayList<>(heartedPlacesPage.getContent());
            int remainingLimit = pageable.getPageSize() - heartedPlacesOrSortedPlaces.size();

            if (remainingLimit > 0) {
                List<Integer> excludedIds = heartedPlacesOrSortedPlaces.stream()
                        .map(Place::getPlaceId)
                        .collect(Collectors.toList());

                Pageable remainingPageable = PageRequest.of(0, remainingLimit);
                Page<Place> recentPlacesPage = placeRepository.findByPlaceIdNotInOrderByCreatedAtDesc(excludedIds, remainingPageable);

                heartedPlacesOrSortedPlaces.addAll(recentPlacesPage.getContent());
            }

            return new PageImpl<>(
                    heartedPlacesOrSortedPlaces.stream().map(PlaceDto::from).collect(Collectors.toList()),
                    pageable,
                    heartedPlacesPage.getTotalElements()
            );
        }

        return heartedPlacesPage.map(PlaceDto::from);
    }
}
