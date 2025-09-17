package com.project.hiptour.sync.application.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.sync.application.job.LoadJob;
import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadJobProcessor {
    private final TourApiPort tourApiPort;
    private final PlaceRepository placeRepository;
    private final PlaceMapperService placeMapperService;
    private final PlaceEntityMapper placeEntityMapper;

    @Transactional
    public void processArea(LoadJob job) throws IOException {
        log.info("지역코드 [{}] 데이터 처리를 시작합니다. 시작 페이지: {}", job.getCurrentAreaCode(), job.getCurrentPageNo());

        while (!job.isApiLimitReached()) {
            String jsonResponse = tourApiPort.fetchPlaceData(job.getCurrentPageNo(), 100, job.getCurrentAreaCode());
            job.incrementApiCallCount();

            if (jsonResponse == null) {
                log.info("지역코드 [{}]의 모든 데이터를 적재했습니다. (API 응답 null)", job.getCurrentAreaCode());
                job.markAreaAsCompleted();
                return;
            }

            List<SyncPlaceDto> dtoList = placeEntityMapper.parseResponseToDtoList(jsonResponse);

            if (CollectionUtils.isEmpty(dtoList)) {
                log.info("지역코드 [{}]의 모든 데이터를 적재했습니다.", job.getCurrentAreaCode());
                job.markAreaAsCompleted();
                return;
            }

            List<Place> placesToSave = dtoList.stream()
                    .map(placeMapperService::mapToEntity)
                    .collect(Collectors.toList());
            // TODO: 저장 방식 변경 및 COMMON의 Entity에 저장 - 수정 필요
            placeRepository.saveAll(placesToSave);

            job.advanceToNextPage();
        }
    }
}
