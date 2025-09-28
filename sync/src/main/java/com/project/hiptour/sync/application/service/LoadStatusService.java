package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.domain.JobExecutionStatus;
import com.project.hiptour.sync.domain.LoadJobStatus;
import com.project.hiptour.sync.domain.LoadJobType;
import com.project.hiptour.sync.infrastructure.persistence.LoadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadStatusService {
    private final LoadStatusRepository loadStatusRepository;

    @Transactional(readOnly = true)
    public Optional<LoadJobStatus> getJobStatus() {
        return loadStatusRepository.findById(LoadJobType.PLACE_LOAD_JOB);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProgress(String areaCode, int lastSucceededPageNo) {
        if (lastSucceededPageNo < 0) {
            lastSucceededPageNo = 0;
        }

        LoadJobStatus currentStatus = loadStatusRepository.findById(LoadJobType.PLACE_LOAD_JOB)
                        .orElseGet(() -> new LoadJobStatus(LoadJobType.PLACE_LOAD_JOB));
        currentStatus.setStatus(JobExecutionStatus.STOPPED);
        currentStatus.setLastSucceededAreaCode(areaCode);
        currentStatus.setLastSucceededPageNo(lastSucceededPageNo);

        loadStatusRepository.save(currentStatus);
        log.info("데이터 적재 작업 진행 상황을 기록했습니다. (AreaCode: {}, LastSucceededPage: {})", areaCode, lastSucceededPageNo);
    }

    @Transactional
    public void markAsFinished() {
        LoadJobStatus finishedStatus = loadStatusRepository.findById(LoadJobType.PLACE_LOAD_JOB)
                .orElseGet(() -> new LoadJobStatus(LoadJobType.PLACE_LOAD_JOB));
        finishedStatus.setStatus(JobExecutionStatus.FINISHED);
        finishedStatus.setLastSucceededAreaCode(null);
        finishedStatus.setLastSucceededPageNo(0);

        loadStatusRepository.save(finishedStatus);
        log.info("모든 데이터 적재 작업을 완료된 것으로 기록했습니다.");
    }
}
