package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.util.TimeProvider;
import com.project.hiptour.sync.domain.SyncJobType;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("성공 - 동기화 및 Overview 작업이 순서대로 올바르게 실행됩니다.")
    void runDailyTasks_Success() {
        int usedApiCallsInSync = 10;
        int remainingBudget = 1000 - usedApiCallsInSync;
        LocalDateTime lastSyncTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime syncStartedTime = LocalDateTime.of(2025, 1, 2, 2, 0);

        given(syncStatusRepository.findById(SyncJobType.PLACE_SYNC)).willReturn(Optional.of(new SyncStatus(SyncJobType.PLACE_SYNC, lastSyncTime)));
        given(timeProvider.now()).willReturn(syncStartedTime);
        given(syncService.syncUpdatedPlaces(lastSyncTime)).willReturn(usedApiCallsInSync);

        dailyJobService.runDailyTasks();

        verify(syncService).syncUpdatedPlaces(lastSyncTime);
        verify(overviewFillService).fillMissingOverviews(remainingBudget);
        verify(syncStatusRepository).save(any(SyncStatus.class));
    }
}
