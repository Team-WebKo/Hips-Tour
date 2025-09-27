package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoadServiceTest {
    @Mock
    private SyncStatusRepository syncStatusRepository;
    @Mock
    private LoadJobProcessor loadJobProcessor;
    @Mock
    private LoadStatusService loadStatusService;

    @InjectMocks
    private LoadService loadService;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(loadService, "areaCodes", List.of("1", "2", "3"));
        ReflectionTestUtils.setField(loadService, "dailyApiCallLimit", 1000);
    }

    @Nested
    @DisplayName("loadAllPlaces 메서드는")
    class Describe_loadAllPlaces {
        @Test
        @DisplayName("이전에 완료된 작업이 없으면, 모든 지역을 처음부터 순회하며 작업을 합니다.")
        void when_no_previous_job_exists_processes_all_areas() throws IOException {
            when(loadStatusService.getJobStatus()).thenReturn(Optional.empty());

            loadService.loadAllPlaces();

            verify(loadJobProcessor, times(3)).processArea(any());
            verify(loadStatusService, times(1)).markAsFinished();
            verify(syncStatusRepository, times(1)).save(any(SyncStatus.class));
        }
    }
}
