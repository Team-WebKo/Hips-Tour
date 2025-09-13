package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.domain.LoadStatus;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import com.project.hiptour.sync.infrastructure.persistence.LoadStatusRepository;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import com.project.hiptour.sync.infrastructure.persistence.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadService {
    private final TourApiPort tourApiPort;
    private final TourPlaceRepository tourPlaceRepository;
    private final PlaceEntityMapper placeEntityMapper;
    private final SyncStatusRepository syncStatusRepository;
    private final LoadStatusRepository loadStatusRepository;
    private final PlaceMapperService placeMapperService;

    @Value("${sync.load.area-codes}")
    private List<String> areaCodes;

    @Value("${sync.load.daily-api-call-limit}")
    private int dailyApiCallLimit;

    private static final String JOB_NAME = "placeLoad";
    private static final String FINISHED_STATUS = "FINISHED";
    private int apiCallCount = 0;

    @Transactional
    public void loadAllPlaces() {
        log.info("TourAPI 전체 데이터 적재를 시작합니다.");
        Optional<LoadStatus> optionalLoadStatus = loadStatusRepository.findById(JOB_NAME);

        if (optionalLoadStatus.isPresent() && FINISHED_STATUS.equals(optionalLoadStatus.get().getLastSucceededAreaCode())) {
            log.info("모든 지역의 데이터 적재가 완료되었습니다.");
            return;
        }

        String startAreaCode = optionalLoadStatus.map(LoadStatus::getLastSucceededAreaCode).orElse(areaCodes.get(0));
        int startPageNo = optionalLoadStatus.map(LoadStatus::getLastSucceededPageNo).map(page -> page + 1).orElse(1);

        boolean startProcessing = false;
        for (String areaCode : areaCodes) {
            if (!startProcessing && !areaCode.equals(startAreaCode)) {
                continue;
            }
            startProcessing = true;

            if (!processAreaCode(areaCode, startPageNo)) {
                return;
            }
            startPageNo = 1;
        }

        saveCurrentStatus(FINISHED_STATUS, 0);
        updateSyncServiceStartTime(LocalDateTime.now());
        log.info("TourAPI 전체 데이터 적재 과정이 완료되었습니다.");
    }

    private boolean processAreaCode(String areaCode, int startPageNo) {
        log.info("지역코드 [{}] 데이터 적재를 시작합니다. 시작 페이지: {}", areaCode, startPageNo);
        int currentPageNo = startPageNo;

        while (true) {
            if (apiCallCount >= dailyApiCallLimit) {
                log.info("일일 API 호출 제한에 도달했습니다. 작업을 종료합니다.");
                saveCurrentStatus(areaCode, currentPageNo - 1);
                return false;
            }

            try {
                String jsonResponse = tourApiPort.fetchPlaceData(currentPageNo, 100, areaCode);
                apiCallCount++;
                if (jsonResponse == null) {
                    break;
                }

                List<SyncPlaceDto> dtoList = placeEntityMapper.parseResponseToDtoList(jsonResponse);
                if (CollectionUtils.isEmpty(dtoList)) {
                    log.info("지역코드 [{}]의 모든 데이터를 적재했습니다.", areaCode);
                    break;
                }

                List<TourPlace> placesToSave = dtoList.stream()
                        .map(placeMapperService::mapToEntity)
                        .collect(Collectors.toList());
                tourPlaceRepository.saveAll(placesToSave);

                currentPageNo++;
            } catch (Exception e) {
                log.error("데이터 적재 중 areaCode={}, pageNo={}에서 오류가 발생했습니다.", areaCode, currentPageNo, e);
                return false;
            }
        }

        return true;
    }

    private void saveCurrentStatus(String areaCode, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }

        LoadStatus currentStatus = new LoadStatus(JOB_NAME, areaCode, pageNo);
        loadStatusRepository.save(currentStatus);
        log.info("현재 작업 상태를 DB에 기록합니다. (areaCode: {}, pageNo: {})", areaCode, pageNo);
    }

    private void updateSyncServiceStartTime(LocalDateTime time) {
        SyncStatus status = new SyncStatus("placeSync", time);
        syncStatusRepository.save(status);
        log.info("전체 적재 작업 완료 시간을 DB에 기록했습니다. (동기화에 사용 될) 기준 시간: {}", time);
    }
}