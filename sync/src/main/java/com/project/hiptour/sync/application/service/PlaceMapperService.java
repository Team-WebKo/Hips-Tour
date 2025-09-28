package com.project.hiptour.sync.application.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.place.repos.RegionInfoRepository;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceMapperService {
    private final PlaceEntityMapper placeEntityMapper;
    private final PlaceRepository placeRepository;
    private final RegionInfoRepository regionInfoRepository;

    public Place mapToEntity(SyncPlaceDto dto) {
        Place place = placeRepository.findByContentId(dto.getContentid())
                .map(existingPlace -> {
                    placeEntityMapper.updateEntityFromDto(existingPlace, dto);
                    return existingPlace;
                })
                .orElseGet(() -> placeEntityMapper.mapDtoToNewEntity(dto));

        if (dto.getAreacode() != null && !dto.getAreacode().isEmpty()) {
            regionInfoRepository.findByAreaCode(dto.getAreacode())
                    .ifPresentOrElse(
                            place::setRegionInfo, () -> log.warn("DB에서 areaCode '{}'에 해당하는 RegionInfo를 찾을 수 없습니다.", dto.getAreacode()));
        }

        return place;
    }
}
