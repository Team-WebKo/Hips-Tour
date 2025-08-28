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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
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

    @Test
    @DisplayName("다음 지역 코드로 넘어갈 때, 페이지 번호는 1로 초기화되어야 한다.")
    void loadAddPlaces_should_reset_pageNo_for_subsequent_areaCodes() throws Exception {
        // Given: 지역코드 1은 2페이지, 지역코드 2는 1페이지의 데이터를 가지도록 설정
        String areaCode1Page1Response = """
                {"response":{"body":{"items":{"item":[{"contentid":"1","addr1":"addr2"}]}}}}
                """;
        String areaCode1Page2Response = """
                {"response":{"body":{"items":{"item":[{"contentid":"2","addr1":"addr2"}]}}}}
                """;
        String areaCode2Page1Response = """
                {"response":{"body":{"items":{"item":[{"contentid":"3","addr1":"addr2"}]}}}}
                """;

        when(tourApiPort.fetchPlaceData(1, 100, "1")).thenReturn(areaCode1Page1Response);
        when(tourApiPort.fetchPlaceData(2, 100, "1")).thenReturn(areaCode1Page2Response);
        when(tourApiPort.fetchPlaceData(3, 100, "1")).thenReturn("{\"response\":{\"body\":{\"items\":{}}}}");

        when(tourApiPort.fetchPlaceData(1, 100, "2")).thenReturn(areaCode2Page1Response);
        when(tourApiPort.fetchPlaceData(2, 100, "2")).thenReturn("{\"response\":{\"body\":{\"items\":{}}}}");

        // When
        loadService.loadAllPlaces();

        // Then: API 호출 시의 pageNo 인자들을 검증
        verify(tourApiPort).fetchPlaceData(1, 100, "1");
        verify(tourApiPort).fetchPlaceData(2, 100, "1");
        verify(tourApiPort).fetchPlaceData(3, 100, "1");
    }

    @Test
    @DisplayName("이전 작업이 중단된 지점부터 데이터 적재를 재개해야 합니다.")
    void should_resume_from_last_succeeded_area_code() throws Exception {
        // Given
        List<String> areaCodes = Arrays.asList("1", "2", "3", "4");
        ReflectionTestUtils.setField(loadService, "areaCodes", areaCodes);

        // 1. 이전 작업 상태를 설정합니다. 지역코드 "2", 페이지 5까지 성공
        String jobName = "placeLoad";
        LoadStatus lastStatus = new LoadStatus(jobName, "2", 5);
        loadStatusRepository.save(lastStatus);

        // 2. tourApiPort의 행동을 정의
        String emptyItemsResponse = """
                {"response":{"body":{"items":{"item":[]}}}}
                """;
        when(tourApiPort.fetchPlaceData(anyInt(), anyInt(), anyString())).thenReturn(emptyItemsResponse);

        // When
        loadService.loadAllPlaces();

        // Then

        // 1. 지역코드 "1"에 대해서는 fetchPlaceData 메서드가 한 번도 호출되지 않았는지 검증
        verify(tourApiPort, never()).fetchPlaceData(anyInt(), anyInt(), eq("1"));

        // 2. 지역코드 "2"에 대해서는 '6페이지'부터 호출이 시작되었는지 검증
        verify(tourApiPort, times(1)).fetchPlaceData(eq(6), anyInt(), eq("2"));

        // 3. 지역코드 "3"에 대해서는 '1페이지'부터 호출이 시작되었는지 검증
        verify(tourApiPort, times(1)).fetchPlaceData(eq(1), anyInt(), eq("3"));

        // 4. 지역코드 "4"에 대해서는 '1페이지'부터 호출이 되었는지 검증
        verify(tourApiPort, times(1)).fetchPlaceData(eq(1), anyInt(), eq("4"));

        // 5. 전체 작업이 완료 -> 최종 상태("FINISHED")가 저장되었는지 검증
        Optional<LoadStatus> finalStatus = loadStatusRepository.findById(jobName);
        assertThat(finalStatus).isPresent();
        assertThat(finalStatus.get().getLastSucceededAreaCode()).isEqualTo("FINISHED");
    }

    @Test
    @DisplayName("일일 API 호출 제한에 도달하면, 작업을 중단하고 마지막 성공 지점을 저장해야 한다")
    void should_stop_and_save_status_when_api_call_limit_is_reached() throws Exception {
        // Given
        ReflectionTestUtils.setField(loadService, "dailyApiCallLimit", 2);

        String nonEmptyResponse = """
                {"response":{"body":{"items":{"item":[{"contentid":"1","addr1":"addr1"}]}}}}
                """;

        when(tourApiPort.fetchPlaceData(anyInt(), anyInt(), anyString())).thenReturn(nonEmptyResponse);

        // When
        loadService.loadAllPlaces();

        // Then
        verify(tourApiPort, times(2)).fetchPlaceData(anyInt(), anyInt(), anyString());

        Optional<LoadStatus> finalStatus = loadStatusRepository.findById("placeLoad");

        // 상태가 저장되었는지 확인
        assertThat(finalStatus).isPresent();

        assertThat(finalStatus.get().getLastSucceededAreaCode()).isNotEqualTo("FINISHED");

        assertThat(finalStatus.get().getLastSucceededAreaCode()).isEqualTo("1");
        assertThat(finalStatus.get().getLastSucceededPageNo()).isEqualTo(2);
    }
}
