package com.project.hiptour.sync.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.PlaceDto;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.dto.TourApiResponseDto;
import com.project.hiptour.sync.entity.SyncLog;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.infra.mapper.TourApiDtoMapper;
import com.project.hiptour.sync.infra.persistence.PlaceRepository;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SyncPlaceCommandHandler {
    private final TourDataApiCaller tourDataApiCaller;
    private final TourApiDtoMapper mapper;
    private final PlaceRepository placeRepository;
    private final SyncLogRepository logRepository;
    private final LogService logService;

    public SyncPlaceCommandHandler(TourDataApiCaller tourDataApiCaller,
                                   TourApiDtoMapper mapper,
                                   PlaceRepository placeRepository,
                                   SyncLogRepository logRepository,
                                   LogService logService) {
        this.tourDataApiCaller = tourDataApiCaller;
        this.mapper = mapper;
        this.placeRepository = placeRepository;
        this.logRepository = logRepository;
        this.logService = logService;
    }

    @Transactional
    public void sync() {
        try {
            String rawData = tourDataApiCaller.fetchPlaceData(1);
            List<TourApiItem> items = mapper.toItemList(rawData);
            List<TourApiDto> dtos = items.stream()
                    .map(TourApiItem::toDto)
                    .toList();
            List<Place> places = mapper.toEntity(dtos);

            placeRepository.saveAll(places);

            logRepository.save(new SyncLog("로그 저장 성공", places.size(), LocalDateTime.now()));

        } catch (Exception e) {
            logService.saveFailLog("저장 실패", e);
            throw new RuntimeException("동기화 실패 : " + e.getMessage(), e);
        }
    }

    public void handle(List<PlaceDto> placeDtoList) {
        List<Place> places = mapper.toEntity(placeDtoList);
        placeRepository.saveAll(places); // 또는 update 방식
    }
}
