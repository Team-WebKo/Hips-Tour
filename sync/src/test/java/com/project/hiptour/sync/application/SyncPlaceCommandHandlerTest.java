package com.project.hiptour.sync.application;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class SyncPlaceCommandHandlerTest {
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SyncPlaceCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = new SyncPlaceCommandHandler(
                apiCaller,
                mapper,
                placeRepository,
                syncLogRepository,
                logService,
                objectMapper
        );
    }

    @Test
    public void syncPlaceData_success_flow() {
        String jsonResponse = "testSample";
        when(apiCaller.fetchPlaceData( 1)).thenReturn(jsonResponse);

        TourApiItem item1 = mock(TourApiItem.class);
        TourApiItem item2 = mock(TourApiItem.class);
        when(mapper.toItemList(jsonResponse)).thenReturn(List.of(item1, item2));

        commandHandler.sync();

        verify(apiCaller).fetchPlaceData(1);
        verify(mapper).toItemList(jsonResponse);
        verify(placeRepository, atLeastOnce()).existsByPlaceNameAndAddress1(anyString(), anyString());
        verify(syncLogRepository).save(any());
    }
}
