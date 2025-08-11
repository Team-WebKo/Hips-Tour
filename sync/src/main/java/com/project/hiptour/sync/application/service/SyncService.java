package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import com.project.hiptour.sync.infrastructure.persistence.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncService {
    private final TourApiPort tourApiPort;
    private final TourPlaceRepository tourPlaceRepository;
    private final PlaceMapper placeMapper;
    private final SyncStatusRepository syncStatusRepository;
    private static final DateTimeFormatter API_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String SYNC_ID = "placeSync";

    @Transactional
    public void syncUpdatedPlaces() {
        log.info("TourAPI 변경분 동기화를 시작합니다.");

        Optional<LocalDateTime> lastSyncTimeOptional = getLastSyncTimeFromDB();
        if (lastSyncTimeOptional.isEmpty()) {
            log.warn("DB에 동기화 기준 시간이 없습니다. 적재 작업이 완료되어야 합니다. 동기화 작업을 건너뜁니다.");
            return;
        }

        LocalDateTime lastSyncTime = lastSyncTimeOptional.get();
        LocalDateTime syncStartedTime = LocalDateTime.now();
        log.info("마지막 동기화 시간: {}", lastSyncTime);

        int pageNo = 1;
        final int numOfRows = 100;
        boolean stopFlag = false;

        while (!stopFlag) {
            try {
                String jsonResponse = tourApiPort.fetchChangedPlaces(lastSyncTime, pageNo, numOfRows);
                if (jsonResponse == null) {
                    break;
                }

                List<SyncPlaceDto> dtoList = placeMapper.parseResponseToDtoList(jsonResponse);

                if (CollectionUtils.isEmpty(dtoList)) {
                    break;
                }

                List<TourPlace> placesToSave = new ArrayList<>();

                for (SyncPlaceDto dto : dtoList) {
                    LocalDateTime itemModifiedTime = LocalDateTime.parse(dto.getModifiedtime(), API_DATE_TIME_FORMATTER);

                    if (itemModifiedTime.isAfter(lastSyncTime)) {
                        placesToSave.add(mapDtoToEntity(dto));
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
                return;
            }
        }

        updateLastSyncTimeToDB(syncStartedTime);
        log.info("TourAPI 변경분 동기화가 완료되었습니다.");
    }

    private TourPlace mapDtoToEntity(SyncPlaceDto dto) {
        String fullAddress = dto.getAddr1() + (dto.getAddr2() != null && !dto.getAddr2().isEmpty() ? " " + dto.getAddr2() : "");
        return TourPlace.builder()
                .id(dto.getContentid())
                .title(dto.getTitle())
                .address(fullAddress)
                .imageUrl(dto.getFirstimage())
                .build();
    }

    private Optional<LocalDateTime> getLastSyncTimeFromDB() {
        return syncStatusRepository.findById(SYNC_ID)
                .map(SyncStatus::getLastSuccessTime);
    }

    private void updateLastSyncTimeToDB(LocalDateTime time) {
        SyncStatus status = new SyncStatus(SYNC_ID, time);
        syncStatusRepository.save(status);
        log.info("동기화 성공 시간을 DB에 기록했습니다. 마지막 성공 시간: {}", time);
    }
}
