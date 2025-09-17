package com.project.hiptour.sync.application.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceMapperService {
    private final PlaceEntityMapper placeEntityMapper;
    private final PlaceRepository placeRepository;

    public Place mapToEntity(SyncPlaceDto dto) {
        return placeRepository.findByContentId(dto.getContentid())
                .map(existingPlace -> {
                    placeEntityMapper.updateEntityFromDto(existingPlace, dto);
                    return existingPlace;
                }).orElseGet(() -> placeEntityMapper.mapDtoToNewEntity(dto));
    }
}
