package com.project.hiptour.sync.application;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.infra.mapper.TourApiDtoMapper;
import com.project.hiptour.sync.infra.persistence.PlaceRepository;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        commandHandler = new SyncPlaceCommandHandler(
                apiCaller,
                mapper,
                placeRepository,
                syncLogRepository,
                logService
        );
    }

    @Test
    public void syncPlaceData_success_flow() {
        String jsonResponse = "testSample";
        when(apiCaller.fetchPlaceData(1)).thenReturn(jsonResponse);

        TourApiItem item1 = mock(TourApiItem.class);
        TourApiItem item2 = mock(TourApiItem.class);
        when(mapper.toItemList(jsonResponse)).thenReturn(List.of(item1, item2));

        when(item1.toDto()).thenReturn(mock(TourApiDto.class));
        when(item2.toDto()).thenReturn(mock(TourApiDto.class));

        when(mapper.toEntity(anyList())).thenReturn(List.of(mock(Place.class)));

        commandHandler.sync();

        verify(apiCaller).fetchPlaceData(1);
        verify(mapper).toItemList(jsonResponse);
        verify(mapper).toEntity(anyList());
        verify(syncLogRepository).save(any());
        System.out.println("흐름 테스트 통과");
    }

    @Test
    void syncPlaceData_whenPlaceSaveFail_thenFailLogIsSave() {
        String rawJson = "testSample";
        when(apiCaller.fetchPlaceData(1)).thenReturn(rawJson);

        TourApiItem itemSample = mock(TourApiItem.class);
        when(itemSample.toDto()).thenReturn(mock(TourApiDto.class));
        when(mapper.toItemList(rawJson)).thenReturn(List.of(itemSample));
        when(mapper.toEntity(anyList())).thenReturn(List.of(mock(Place.class)));

        when(placeRepository.saveAll(anyList())).thenThrow(new RuntimeException("저장 실패"));

        try {
            commandHandler.sync();
        } catch (RuntimeException e) {

        }

        verify(logService).saveFailLog(contains("저장 실패"), any(Exception.class));
        System.out.println("실패 로그 테스트 통과");
    }
}
