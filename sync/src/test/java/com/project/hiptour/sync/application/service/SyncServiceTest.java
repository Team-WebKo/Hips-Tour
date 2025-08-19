package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.infrastructure.persistence.TourPlaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Transactional
public class SyncServiceTest {
    @Autowired
    private SyncService syncService;

    @Autowired
    private TourPlaceRepository tourPlaceRepository;

    @MockBean
    private TourApiPort tourApiPort;

    private static final DateTimeFormatter API_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @DisplayName("TourAPI에서 변경된 장소 목록을 가져와 DB에 동기화한다.")
    @Test
    void syncUpdatePlaces_SaveNewAndUpdatePlaces() throws Exception {
        // given
        LocalDateTime lastSyncTime = LocalDateTime.of(2025, 8, 1, 0, 0, 0);

        // 기존에 저장되어 있던 장소
        TourPlace existingPlace = TourPlace.builder()
                .contentId("1")
                .title("기존 장소")
                .address("기존 주소")
                .modifiedTime(lastSyncTime.minusDays(1))
                .build();
        tourPlaceRepository.save(existingPlace);

        String newPlaceModifiedTime = API_DATE_TIME_FORMATTER.format(lastSyncTime.plusHours(1));
        String updatedplaceModifiedTime = API_DATE_TIME_FORMATTER.format(lastSyncTime.plusHours(2));
        String oldPlaceModifiedTime = API_DATE_TIME_FORMATTER.format(lastSyncTime.minusHours(1));

        String dummyJsonResponse = createDummyJsonResponse(
                new DummyPlace("2", "새로운 장소", "새로운 주소", newPlaceModifiedTime),
                new DummyPlace("1", "업데이트된 장소", "업데이트된 주소", updatedplaceModifiedTime),
                new DummyPlace("3", "오래된 장소", "오래된 주소", oldPlaceModifiedTime)
        );

        when(tourApiPort.fetchChangedPlaces(any(LocalDateTime.class)))
    }
}
