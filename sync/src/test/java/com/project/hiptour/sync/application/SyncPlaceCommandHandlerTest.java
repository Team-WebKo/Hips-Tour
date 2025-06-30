package com.project.hiptour.sync.application;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.infra.api.TourApiCaller;
import com.project.hiptour.sync.infra.mapper.PlaceMapper;
import com.project.hiptour.sync.infra.mapper.TourApiDtoMapper;
import com.project.hiptour.sync.infra.persistence.PlaceRepository;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncPlaceCommandHandlerTest {
    @Mock
    private TourDataApiCaller apiCaller;

    @Mock
    private TourApiDtoMapper mapper;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private SyncLogRepository syncLogRepository;

    @Mock
    private LogService logService;

    @InjectMocks
    private SyncPlaceCommandHandler commandHandler;

    // 활용 테스트 진행!
//    @Mock
//    private TourApiCaller tourApiCaller;

    private PlaceMapper placeMapper = new PlaceMapper();

    @BeforeEach
    void setUp() {
        commandHandler = new SyncPlaceCommandHandler(
                apiCaller,
                mapper,
                placeRepository,
                syncLogRepository,
                logService,
                placeMapper
        );
    }

    @DisplayName("시렞 흐름만 검증하는 기본 성공 테스트")
    @Test
    public void syncPlaceData_success_flow() {
        String jsonResponse = "testSample";
        when(apiCaller.fetchPlaceData(1)).thenReturn(jsonResponse);

        TourApiItem item1 = mock(TourApiItem.class);
        TourApiItem item2 = mock(TourApiItem.class);
        when(mapper.toItemList(jsonResponse)).thenReturn(List.of(item1, item2));

        when(mapper.toEntity(anyList())).thenReturn(List.of(mock(Place.class)));

        commandHandler.sync();

        verify(apiCaller).fetchPlaceData(1);
        verify(mapper).toItemList(jsonResponse);
        verify(mapper).toEntity(anyList());
        verify(syncLogRepository).save(any());
        System.out.println("흐름 테스트 통과");
    }

    @DisplayName("Mock API 응답을 기반으로 전체 매핑 흐름 테스트")
    @Test
    public void syncPlaceData_success_flow_mockApi() {
        String jsonResponse = "fake_json_response";
        when(apiCaller.fetchPlaceData(1)).thenReturn(jsonResponse);

        TourApiItem mockItem = mock(TourApiItem.class);
        when(mapper.toItemList(jsonResponse)).thenReturn(List.of(mockItem));

        TourApiDto dto = mock(TourApiDto.class);
        when(mapper.toDtoList(List.of(mockItem))).thenReturn(List.of(dto));

        Place mockPlace = mock(Place.class);
        when(mapper.toEntity(List.of(dto))).thenReturn(List.of(mockPlace));

        commandHandler.sync();

        verify(apiCaller).fetchPlaceData(1);
        verify(mapper).toItemList(jsonResponse);
        verify(mapper).toEntity(List.of(dto));
        verify(syncLogRepository).save(any());
    }

    @DisplayName("장소 저장 실패 시 실패 로그가 기록되어야 한다")
    @Test
    void syncPlaceData_whenPlaceSaveFail_thenFailLogIsSave() {
        String rawJson = "testSample";
        when(apiCaller.fetchPlaceData(1)).thenReturn(rawJson);

        TourApiItem itemSample = mock(TourApiItem.class);
        when(mapper.toItemList(rawJson)).thenReturn(List.of(itemSample));
        when(mapper.toDtoList(anyList())).thenReturn(List.of(mock(TourApiDto.class)));
        when(mapper.toEntity(anyList())).thenReturn(List.of(mock(Place.class)));

        when(placeRepository.saveAll(anyList()))
                .thenThrow(new RuntimeException("저장 실패"));

        try {
            commandHandler.sync();
        } catch (RuntimeException ignored) {}

        //테스트의 중요한 부분 - commandHandler.sync()가 실패했을 시 실패 로그를 남기기위한 로직이 수행되는가?
        verify(logService).saveFailLog(eq("PLACE"), any(Exception.class));
        System.out.println("저장 실패 테스트 통과");
    }
}
