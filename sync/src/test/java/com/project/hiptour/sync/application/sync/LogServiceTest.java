package com.project.hiptour.sync.application.sync;

import com.project.hiptour.sync.application.AlarmService;
import com.project.hiptour.sync.application.LogService;
import com.project.hiptour.sync.entity.SyncLog;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
    @Mock
    private SyncLogRepository syncLogRepository;

    @Mock
    private AlarmService alarmService;

    @InjectMocks
    private LogService logService;

    @BeforeEach
    void setUp() {
        syncLogRepository = mock(SyncLogRepository.class);
        alarmService = mock(AlarmService.class);
        logService = new LogService(syncLogRepository, alarmService);
    }

    @DisplayName("최근 23시간 내 5회 이상 실패하면 알림이 호출됩니다.")
    @Test
    void alarm_is_called_when_5_failures_occur_within_23_hours() {
        String syncType = "PLACE";
        Exception ex = new RuntimeException("동기화 실패");

        when(syncLogRepository.findTopBySyncTypeAndStatusOrderByLastSuccessSyncAtDesc(eq(syncType), eq("SUCCESS")))
                .thenReturn(Optional.of(new SyncLog(syncType, "SUCCESS", 10, LocalDateTime.now().minusHours(3), LocalDateTime.now().minusHours(3), null)));

        when(syncLogRepository.countBySyncTypeAndStatusAndCreatedAtAfter(eq(syncType), eq("FAIL"), any()))
                .thenReturn(5L);

        logService.saveFailLog(syncType, ex);

        verify(alarmService, times(1)).notifyAdmin(
                contains("동기화 연속 실패"),
                contains("5회")
        );
    }

    @DisplayName("실패 횟수가 5회 미만일 경우 알람은 호출되지 않습니다.")
    @Test
    void alarm_not_called_less_failCount() {
        String syncType = "PLACE";
        Exception ex = new RuntimeException("실패");

        when(syncLogRepository.findTopBySyncTypeAndStatusOrderByLastSuccessSyncAtDesc(eq(syncType), eq("SUCCESS")))
                .thenReturn(Optional.empty());

        when(syncLogRepository.countBySyncTypeAndStatusAndCreatedAtAfter(eq(syncType), eq("FAIL"), any()))
                .thenReturn(2L);

        logService.saveFailLog(syncType, ex);

        verify(alarmService, never()).notifyAdmin(any(), any());
    }
}
