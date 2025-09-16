package com.project.hiptour.common.usercase.place;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.exception.place.PlaceNotFoundException;
import com.project.hiptour.common.web.place.PlaceDto;
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
        return placeRepository.findAllByOrderByCreatedAtDesc(pageable).map(PlaceDto::from);
    }
}
