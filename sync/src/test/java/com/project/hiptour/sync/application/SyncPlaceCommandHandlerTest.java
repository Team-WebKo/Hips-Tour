package com.project.hiptour.sync.application;

import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.infra.mapper.TourApiDtoMapper;
import com.project.hiptour.sync.infra.persistence.PlaceRepository;
import com.project.hiptour.sync.infra.persistence.SyncLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SyncPlaceCommandHandlerTest {
    @Mock
    private TourDataApiCaller apiCaller;

    @Mock
    private TourApiDtoMapper mapper;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private SyncLogRepository syncLogRepository;

    @InjectMocks
    private SyncPlaceCommandHandler commandHandler;

    @Test
    void syncPlaceData_success_save() {
        String jsonResponse = "";
        when(apiCaller.fetchPlaceData( 1)).thenReturn(jsonResponse);

        TourApiItem item1 = mock(TourApiItem.class);
        TourApiItem item2 = mock(TourApiItem.class);

        TourApiDto dto1 = new TourApiDto("id1", "place1", "인천광역시", "서구", 126.7, 37.5);
        TourApiDto dto2 = new TourApiDto("id2", "place2", "인천광역시", "계양구", 126.75, 37.55);

        when(item1.toDto()).thenReturn(dto1);
        when(item2.toDto()).thenReturn(dto2);

        when(mapper.toItemList(jsonResponse)).thenReturn(List.of(item1, item2));
    }
}
