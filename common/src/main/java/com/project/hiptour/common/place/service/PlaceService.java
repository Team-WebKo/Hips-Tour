package com.project.hiptour.common.place.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<PlaceDto> findPlacesByCategoryId(Integer categoryId) {
        List<Place> places = placeRepository.findByCategoryCategoryId(categoryId);
        return places.stream()
                .map(PlaceDto::from)
                .collect(Collectors.toList());
    }
}
