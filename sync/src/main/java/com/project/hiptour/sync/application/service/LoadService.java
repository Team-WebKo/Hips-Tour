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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadService {
    private final TourApiPort tourApiPort;
    private final TourPlaceRepository tourPlaceRepository;
    private final PlaceMapper placeMapper;
    private final SyncStatusRepository syncStatusRepository;

    @Transactional
    public void loadAllPlaces() {
        log.info("TourAPI 전체 데이터 적재를 시작합니다.");
        int pageNo = 1;
        final int numOfRows = 100;
        int totalSavedCount = 0;

        while (true) {
            try {
                log.info("{} 페이지 데이터 적재를 시도합니다.", pageNo);
                String jsonResponse = tourApiPort.fetchPlaceData(pageNo, numOfRows);

                if (jsonResponse == null) {
                    log.error("TourAPI로부터 응답을 받지 못했습니다. pageNo: {}", pageNo);
                    break;
                }

                List<SyncPlaceDto> dtoList = placeMapper.parseResponseToDtoList(jsonResponse);
                if (CollectionUtils.isEmpty(dtoList)) {
                    log.info("더 이상 가져올 데이터가 없습니다. 적재 작업을 종료합니다.");
                    break;
                }

                List<TourPlace> entityList = placeMapper.mapDtoListToEntityList(dtoList);
                tourPlaceRepository.saveAll(entityList);
                totalSavedCount += entityList.size();
                pageNo++;

                log.info("현재 페이지(pageNo={})에서 {}개의 정보를 적재하였습니다. (누적: {})", pageNo, entityList.size(), totalSavedCount);

            } catch (Exception e) {
                log.info("데이터 적재 중 pageNo={}에서 오류가 발생했습니다.", pageNo, e);
                return;
            }
        }

        updateLastSyncTimeToDB(LocalDateTime.now());
        log.info("TourAPI 전체 데이터 적재 완료하였습니다. 총 {}개의 데이터가 저장되었습니다.", totalSavedCount);
    }

    private void updateLastSyncTimeToDB(LocalDateTime time) {
        final String SYNC_ID = "placeSync";
        SyncStatus status = new SyncStatus(SYNC_ID, time);
        syncStatusRepository.save(status);
        log.info("전체 적재 작업 완료 시간을 DB에 기록했습니다. (동기화에 사용될) 기준 시간: {}", time);
    }
}
