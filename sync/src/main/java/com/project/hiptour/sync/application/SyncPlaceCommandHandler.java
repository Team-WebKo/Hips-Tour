package com.project.hiptour.sync.application;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.sync.dto.PlaceDto;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.entity.SyncLog;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.infra.mapper.PlaceMapper;
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
    private final PlaceMapper placeMapper;

    public SyncPlaceCommandHandler(TourDataApiCaller tourDataApiCaller,
                                   TourApiDtoMapper mapper,
                                   PlaceRepository placeRepository,
                                   SyncLogRepository logRepository,
                                   LogService logService,
                                   PlaceMapper placeMapper) {
        this.tourDataApiCaller = tourDataApiCaller;
        this.mapper = mapper;
        this.placeRepository = placeRepository;
        this.logRepository = logRepository;
        this.logService = logService;
        this.placeMapper = placeMapper;
    }

    @Transactional
    public void sync() {

        try {
            String rawData = tourDataApiCaller.fetchPlaceData(1);
            List<TourApiItem> items = mapper.toItemList(rawData);
            List<TourApiDto> dtos = mapper.toDtoList(items);
            List<Place> places = mapper.toEntity(dtos);

            placeRepository.saveAll(places);

            SyncLog log = SyncLog.success("PLACE", places.size(), LocalDateTime.now());
            logRepository.save(log);

        } catch (Exception e) {
            logService.saveFailLog("PLACE", e);
            throw new RuntimeException("동기화 실패 : " + e.getMessage(), e);
        }
    }

    public void handle(List<PlaceDto> placeDtoList) {
        List<Place> places = placeMapper.toEntity(placeDtoList);
        placeRepository.saveAll(places);
    }
}
