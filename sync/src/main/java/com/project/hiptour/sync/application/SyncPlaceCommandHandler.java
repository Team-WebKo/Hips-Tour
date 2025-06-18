package com.project.hiptour.sync.application;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.dto.TourApiResponseDto;
import com.project.hiptour.sync.entity.SyncLog;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.external.mapper.TourApiParser;
import com.project.hiptour.sync.infra.mapper.TourApiDtoMapper;
import com.project.hiptour.sync.infra.persistence.PlaceCommandRepository;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SyncPlaceCommandHandler {
    private final TourDataApiCaller tourDataApiCaller;
    private final TourApiParser parser;
    private final TourApiDtoMapper mapper;
    private final PlaceCommandRepository placeCommandRepository;
    private final SyncLogRepository logRepository;

    public SyncPlaceCommandHandler(TourDataApiCaller tourDataApiCaller,
                                   TourApiParser parser,
                                   TourApiDtoMapper mapper,
                                   PlaceCommandRepository placeCommandRepository,
                                   SyncLogRepository logRepository) {
        this.tourDataApiCaller = tourDataApiCaller;
        this.parser = parser;
        this.mapper = mapper;
        this.placeCommandRepository = placeCommandRepository;
        this.logRepository = logRepository;
    }

    public void sync() {
        try {
            String rawData = tourDataApiCaller.fetchPlaceData(1);

            TourApiResponseDto responseDto = parser.parse(rawData);
            List<TourApiItem> items = responseDto.getResponse().getBody().getItems().getItem();

            List<TourApiDto> dtos = parser.convertToDtoList(items);

            List<Place> places = mapper.toEntity(dtos);

            placeCommandRepository.saveAll(places);

            logRepository.save(new SyncLog("로그 저장 성공", places.size(), LocalDateTime.now()));

        } catch (Exception e) {
            logRepository.save(new SyncLog("저장 실패", 0, LocalDateTime.now()));
            throw new RuntimeException("동기화 실패 : " + e.getMessage(), e);
        }
    }
}
