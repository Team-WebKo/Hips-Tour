package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.job.LoadJob;
import com.project.hiptour.sync.domain.LoadStatus;
import com.project.hiptour.sync.domain.SyncJobType;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadService {
    private final SyncStatusRepository syncStatusRepository;
    private final LoadJobProcessor loadJobProcessor;
    private final LoadStatusService loadStatusService;

    @Value("${sync.load.area-codes}")
    private List<String> areaCodes;

    @Value("${sync.load.daily-api-call-limit}")
    private int dailyApiCallLimit;

    public void loadAllPlaces() {
        log.info("TourAPI 전체 데이터 적재를 시작합니다.");
        Optional<LoadStatus> jobStatus = loadStatusService.getJobStatus();

        if (jobStatus.isPresent() && LoadStatusService.FINISHED_STATUS.equals(jobStatus.get().getLastSucceededAreaCode())) {
            log.info("모든 지역의 데이터 적재가 이미 완료되었습니다.");
            return;
        }

        LoadJob job = new LoadJob(dailyApiCallLimit, jobStatus, areaCodes);

        int startIndex = areaCodes.indexOf(job.getCurrentAreaCode());

        if (startIndex == -1) {
            log.error("설정된 시작 지역코드 {}를 area-codes 목록에서 찾을 수 없습니다.", job.getCurrentAreaCode());
            return;
        }

        try {
            for (int i = startIndex; i < areaCodes.size(); i++) {
                String currentAreaCode = areaCodes.get(i);

                if (i > startIndex) {
                    job.startArea(currentAreaCode);
                }

                loadJobProcessor.processArea(job);

                if (job.isApiLimitReached()) {
                    log.info("일일 API 호출 제한에 도달하여 작업을 중단합니다.");
                    loadStatusService.saveProgress(job.getCurrentAreaCode(), job.getLastSucceededPageNo());
                    return;
                }
            }

            log.info("모든 지역의 데이터 적재를 성공적으로 완료했습니다.");
            loadStatusService.markAsFinished();
            updateSyncServiceStartTime(LocalDateTime.now());

        } catch (Exception e) {
            log.error("데이터 적재 작업 중 오류가 발생하여 중단합니다.", e);
            loadStatusService.saveProgress(job.getCurrentAreaCode(), job.getLastSucceededPageNo());
        }
    }

    private void updateSyncServiceStartTime(LocalDateTime time) {
        SyncStatus status = new SyncStatus(SyncJobType.PLACE_SYNC, time);
        syncStatusRepository.save(status);
        log.info("전체 적재 작업 완료 시간을 DB에 기록했습니다. (동기화에 사용 될) 기준 시간: {}", time);
    }
}