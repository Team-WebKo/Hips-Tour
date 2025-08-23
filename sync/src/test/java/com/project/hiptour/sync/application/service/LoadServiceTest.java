package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.domain.LoadStatus;
import com.project.hiptour.sync.domain.SyncStatus;
import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.infrastructure.persistence.LoadStatusRepository;
import com.project.hiptour.sync.infrastructure.persistence.SyncStatusRepository;
import com.project.hiptour.sync.infrastructure.persistence.TourPlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LoadServiceTest {
    @MockBean
    private TourApiPort tourApiPort;

    @Autowired
    private LoadService loadService;

    @Autowired
    private LoadStatusRepository loadStatusRepository;

    @Autowired
    private TourPlaceRepository tourPlaceRepository;

    @Autowired
    private SyncStatusRepository syncStatusRepository;

    @BeforeEach
    void setUp() {
        tourPlaceRepository.deleteAll();
        loadStatusRepository.deleteAll();
        syncStatusRepository.deleteAll();
        ReflectionTestUtils.setField(loadService, "areaCodes", List.of("1", "2"));
    }

    @Test
    @DisplayName("초기 상태에서 전체 데이터 적재 시, API 응답에 따라 모든 데이터를 적재하고 완료 상태를 기록해야 한다.")
    void loadAllPlaces_initialLoad_success() throws Exception {
        // Given
        String areaCode1Page1Response = """
                {"response":{"header":{"resultCode":"0000","resultMsg":"OK"},"body":{"items":{"item":[{"contentid":"1","addr1":"addr1"}]},"numOfRows":1,"pageNo":1,"totalCount":1}}}
                """;
        String areaCode2Page1Response = """
                {"response":{"header":{"resultCode":"0000","resultMsg":"OK"},"body":{"items":{"item":[{"contentid":"2","addr1":"addr2"}]},"numOfRows":1,"pageNo":1,"totalCount":1}}}
                """;

        // 지역코드 1: 1페이지 데이터 반환, 2페이지는 데이터 없음(null)
        when(tourApiPort.fetchPlaceData(1, 100, "1")).thenReturn(areaCode1Page1Response);
        when(tourApiPort.fetchPlaceData(2, 100, "1")).thenReturn(null);
        // 지역코드 2: 1페이지 데이터 반환, 2페이지는 데이터 없음(null)
        when(tourApiPort.fetchPlaceData(1, 100, "2")).thenReturn(areaCode2Page1Response);
        when(tourApiPort.fetchPlaceData(2, 100, "2")).thenReturn(null);

        // When
        loadService.loadAllPlaces();;

        // Then: 결과 검증
        // 1. TourPlaceReposiroty에 2개의 장소가 저장되어 있는지 확인
        List<TourPlace> savedPlaces = tourPlaceRepository.findAll();
        assertThat(savedPlaces.get(0).getContentId()).isEqualTo("1");
        assertThat(savedPlaces.get(1).getContentId()).isEqualTo("2");

        // 2. TourApiPort가 총 4번 호출되었는지 확인 (지역코드별 2번씩)
        verify(tourApiPort, times(4)).fetchPlaceData(anyInt(), anyInt(), anyString());

        // 3. LoadStatus가 'FINISHED'로 저장되었는지 확인
        Optional<LoadStatus> loadStatus = loadStatusRepository.findById("placeLoad");
        assertThat(loadStatus).isPresent();
        assertThat(loadStatus.get().getLastSucceededAreaCode()).isEqualTo("FINISHED");

        // 4. SyncStatus가 생성되고, 마지막 동기화 시간이 기록되었는지 확인
        Optional<SyncStatus> syncStatus = syncStatusRepository.findById("placeSync");
        assertThat(syncStatus).isPresent();
        assertThat(syncStatus.get().getLastSuccessTime()).isNotNull();
    }

    @Test
    @DisplayName("모든 지역 데이터 적재가 이미 완료된 경우, 작업이 즉시 종료되어야 합니다.")
    void LoadAllPlaces_already_success_case() {
        // Given
        LoadStatus finishedStatus = new LoadStatus("placeLoad", "FINISHED", 0);
        loadStatusRepository.save(finishedStatus);

        // When: 전체 데이터 적재 서비스를 시작합니다.
        loadService.loadAllPlaces();

        // Then: API 호출이 전혀 발생하지 않았는지 확인합니다.
        verify(tourApiPort, never()).fetchPlaceData(anyInt(), anyInt(), anyString());
    }
}
