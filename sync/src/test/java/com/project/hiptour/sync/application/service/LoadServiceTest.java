package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.job.LoadJob;
import com.project.hiptour.sync.domain.JobExecutionStatus;
import com.project.hiptour.sync.domain.LoadJobStatus;
import com.project.hiptour.sync.domain.LoadJobType;
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

import static org.assertj.core.api.Assertions.assertThat;
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
            doNothing().when(loadJobProcessor).processArea(any(LoadJob.class));

            loadService.loadAllPlaces();

            verify(loadJobProcessor, times(3)).processArea(any(LoadJob.class));
            verify(loadStatusService, times(1)).markAsFinished();
            verify(syncStatusRepository, times(1)).save(any(SyncStatus.class));
        }

        @Test
        @DisplayName("모든 작업이 이미 완료된 상태라면, 아무 작업도 수행하지 않습니다.")
        void when_job_is_already_finished_does_nothing() throws IOException {
            LoadJobStatus finishedStatus = new LoadJobStatus(LoadJobType.PLACE_LOAD_JOB);
            finishedStatus.setStatus(JobExecutionStatus.FINISHED);
            when(loadStatusService.getJobStatus()).thenReturn(Optional.of(finishedStatus));

            loadService.loadAllPlaces();

            verifyNoInteractions(loadJobProcessor);
            verifyNoInteractions(syncStatusRepository);
            verify(loadStatusService, never()).markAsFinished();
        }

        @Test
        @DisplayName("이전에 특정 지역에서 중단되었다면, 중단된 지역부터 작업을 재개합니다.")
        void when_job_was_stopped_resumes_from_that_point() throws IOException {
            LoadJobStatus stoppedStatus = new LoadJobStatus(LoadJobType.PLACE_LOAD_JOB);
            stoppedStatus.setStatus(JobExecutionStatus.STOPPED);
            stoppedStatus.setLastSucceededAreaCode("2");
            stoppedStatus.setLastSucceededPageNo(5);

            when(loadStatusService.getJobStatus()).thenReturn(Optional.of(stoppedStatus));
            java.util.concurrent.atomic.AtomicInteger callCount = new java.util.concurrent.atomic.AtomicInteger(0);
            doAnswer(invocation -> {
                LoadJob job = invocation.getArgument(0);
                int currnetCall = callCount.incrementAndGet();

                if (currnetCall == 1) {
                    assertThat(job.getCurrentAreaCode()).isEqualTo("2");
                    assertThat(job.getCurrentPageNo()).isEqualTo(6);
                } else if (currnetCall == 2) {
                    assertThat(job.getCurrentAreaCode()).isEqualTo("3");
                    assertThat(job.getCurrentPageNo()).isEqualTo(1);
                }
                return null;
            }).when(loadJobProcessor).processArea(any(LoadJob.class));

            loadService.loadAllPlaces();

            verify(loadJobProcessor, times(2)).processArea(any(LoadJob.class));
        }
    }
}
