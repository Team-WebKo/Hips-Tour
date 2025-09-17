package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.domain.LoadStatus;
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
    private static final String JOB_NAME = "placeLoad";
    public static final String FINISHED_STATUS = "FINISHED";
    private final LoadStatusRepository loadStatusRepository;

    @Transactional(readOnly = true)
    public Optional<LoadStatus> getJobStatus() {
        return loadStatusRepository.findById(JOB_NAME);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW);
    public void saveProgress(String areaCode, int lastSucceededPageNo) {
        if (lastSucceededPageNo < 0) {
            lastSucceededPageNo = 0;
        }

        LoadStatus currendStatus = new LoadStatus(JOB_NAME, areaCode, lastSucceededPageNo);
        loadStatusRepository.save(currendStatus);
        log.info("데이터 적재 작업 진행 상황을 기록했습니다. (AreaCode: {}, LastSucceededPage: {}", areaCode, lastSucceededPageNo);
    }

    @Transactional
    public void markAsFinished() {
        LoadStatus finishedStatus = new LoadStatus(JOB_NAME, FINISHED_STATUS, 0);
        loadStatusRepository.save(finishedStatus);
        log.info("모든 데이터 적재 작업을 완료된 것으로 기록했습니다.");
    }
}
