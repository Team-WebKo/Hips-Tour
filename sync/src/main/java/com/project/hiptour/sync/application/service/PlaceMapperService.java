package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import com.project.hiptour.sync.infrastructure.persistence.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceMapperService {
    private final TourPlaceRepository tourPlaceRepository;
    private final PlaceEntityMapper placeEntityMapper;

    public TourPlace mapToEntity(SyncPlaceDto dto) {
        return tourPlaceRepository.findByContentId(dto.getContentid())
                .map(existingPlace -> {
                    placeEntityMapper.updateEntityFromDto(existingPlace, dto);
                    return existingPlace;
                })
                .orElseGet(() -> placeEntityMapper.mapDtoToNewEntity(dto));
    }
}
