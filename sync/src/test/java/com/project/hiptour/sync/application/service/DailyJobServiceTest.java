package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.util.TimeProvider;
import com.project.hiptour.sync.domain.JobExecutionStatus;
import com.project.hiptour.sync.domain.SyncJobType;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DailyJobService 테스트")
public class DailyJobServiceTest {
    @InjectMocks
    private DailyJobService dailyJobService;

    @Mock
    private SyncService syncService;

    @Mock
    private OverviewFillService overviewFillService;

    @Mock
    private SyncStatusRepository syncStatusRepository;

    @Mock
    private TimeProvider timeProvider;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(dailyJobService, "dailyApiCallLimit", 1000);
    }

    private SyncStatus createDefaultSyncStatus() {
        SyncStatus status = new SyncStatus(SyncJobType.PLACE_SYNC);
        status.setStatus(JobExecutionStatus.FINISHED);
        status.setLastSuccessTime(LocalDateTime.of(2025, 1, 1, 0, 0));
        return status;
    }

    @Nested
    @DisplayName("runDailyTasks 메서드는")
    class Describe_runDailyTasks {
        @Test
        @DisplayName("정상 상태에서 실행 시, 작업을 모두 수행하고 상태를 RUNNING에서 FINISHED로 변경합니다.")
        void success_path() {
            SyncStatus initialStatus = createDefaultSyncStatus();
            LocalDateTime syncStartedTime = LocalDateTime.of(2025, 1, 2, 2, 0);
            int usedApiCalls = 10;

            given(syncStatusRepository.findById(SyncJobType.PLACE_SYNC)).willReturn(Optional.of(initialStatus));
            given(timeProvider.now()).willReturn(syncStartedTime);
            given(syncService.syncUpdatedPlaces(any(LocalDateTime.class))).willReturn(usedApiCalls);

            dailyJobService.runDailyTasks();

            ArgumentCaptor<SyncStatus> statusCaptor = ArgumentCaptor.forClass(SyncStatus.class);

            verify(syncStatusRepository, times(2)).save(statusCaptor.capture());

            List<SyncStatus> savedStatuses = statusCaptor.getAllValues();

            assertThat(savedStatuses.get(0).getStatus()).isEqualTo(JobExecutionStatus.RUNNING);
            assertThat(savedStatuses.get(1).getStatus()).isEqualTo(JobExecutionStatus.FINISHED);
            assertThat(savedStatuses.get(1).getLastSuccessTime()).isEqualTo(syncStartedTime);

            verify(syncService).syncUpdatedPlaces(initialStatus.getLastSuccessTime());
            verify(overviewFillService).fillMissingOverviews(1000 - usedApiCalls);
        }

        @Test
        @DisplayName("이전 작업이 RUNNING 상태일 경우, 중복 실행을 방지하고 아무 작업도 하지 않습니다.")
        void skips_when_already_running() {
            SyncStatus runningStatus = createDefaultSyncStatus();
            runningStatus.setStatus(JobExecutionStatus.RUNNING);

            given(syncStatusRepository.findById(SyncJobType.PLACE_SYNC)).willReturn(Optional.of(runningStatus));

            dailyJobService.runDailyTasks();

            verify(syncStatusRepository, never()).save(any());
            verify(syncService, never()).syncUpdatedPlaces(any());
            verify(overviewFillService, never()).fillMissingOverviews(anyInt());
        }

        @Test
        @DisplayName("DB에 상태 정보가 전혀 없으면(최초 실행), 기본값으로 작업을 수행합니다.")
        void first_run_default() {
            given(syncStatusRepository.findById(SyncJobType.PLACE_SYNC)).willReturn(Optional.empty());
            given(timeProvider.now()).willReturn(LocalDateTime.of(2025, 1, 2, 2, 0));
            given(syncService.syncUpdatedPlaces(any(LocalDateTime.class))).willReturn(5);

            dailyJobService.runDailyTasks();

            ArgumentCaptor<LocalDateTime> timeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

            verify(syncService).syncUpdatedPlaces(timeCaptor.capture());
            assertThat(timeCaptor.getValue().getYear()).isEqualTo(2000);

            ArgumentCaptor<SyncStatus> statusCaptor = ArgumentCaptor.forClass(SyncStatus.class);

            verify(syncStatusRepository, times(2)).save(statusCaptor.capture());

            assertThat(statusCaptor.getAllValues().get(0).getStatus()).isEqualTo(JobExecutionStatus.RUNNING);
            assertThat(statusCaptor.getAllValues().get(1).getStatus()).isEqualTo(JobExecutionStatus.FINISHED);
        }
    }
}
