package com.project.hiptour.common.place.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
