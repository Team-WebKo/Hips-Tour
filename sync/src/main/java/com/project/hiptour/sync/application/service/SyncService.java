package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import com.project.hiptour.sync.infrastructure.persistence.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncService {
    private final TourApiPort tourApiPort;
    private final TourPlaceRepository tourPlaceRepository;
    private final PlaceEntityMapper placeEntityMapper;
    private final PlaceMapperService placeMapperService;
    private static final DateTimeFormatter API_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Transactional(propagation = Propagation.REQUIRED)
    public void syncUpdatedPlaces(LocalDateTime lastSyncTime) {
        log.info("TourAPI 변경분 동기화를 시작합니다.");

        int pageNo = 1;
        final int numOfRows = 100;
        boolean stopFlag = false;

        while (!stopFlag) {
            try {
                String jsonResponse = tourApiPort.fetchChangedPlaces(lastSyncTime, pageNo, numOfRows);

                List<SyncPlaceDto> dtoList = null;
                if (jsonResponse != null) {
                    dtoList = placeEntityMapper.parseResponseToDtoList(jsonResponse);
                }

                if (CollectionUtils.isEmpty(dtoList)) {
                    log.info("동기화할 새로운 데이터가 없습니다. 작업을 종료합니다.");
                    break;
                }

                List<TourPlace> placesToSave = new ArrayList<>();

                for (SyncPlaceDto dto : dtoList) {
                    LocalDateTime itemModifiedTime = LocalDateTime.parse(dto.getModifiedtime(), API_DATE_TIME_FORMATTER);

                    if (itemModifiedTime.isAfter(lastSyncTime)) {
                        placesToSave.add(placeMapperService.mapToEntity(dto));
                    } else {
                        stopFlag = true;
                        break;
                    }
                }

                if (!placesToSave.isEmpty()) {
                    tourPlaceRepository.saveAll(placesToSave);
                }
                pageNo++;

            } catch (Exception e) {
                log.error("변경분 동기화 중 pageNo={}에서 오류가 발생했습니다.", pageNo, e);
                throw new RuntimeException("변경분 동기화 중 오류가 발생했습니다.", e);
            }
        }
    }
}