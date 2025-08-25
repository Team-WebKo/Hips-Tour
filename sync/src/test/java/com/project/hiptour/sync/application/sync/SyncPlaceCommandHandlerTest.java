package com.project.hiptour.sync.application.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.sync.application.LogService;
import com.project.hiptour.sync.application.SyncPlaceCommandHandler;
import com.project.hiptour.sync.application.util.TourApiTestJsonFactory;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.dto.TourApiResponseDto;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @DisplayName("실제 TourAPI 응답 JSON이 DTO에 정상 매핑되는지 확인하는 테스트입니다.")
    @Test
    void test_TourApiResponseDto_parsing_with_payload() throws Exception {
        TourApiItem mockItem = new TourApiItem();
        mockItem.setTitle("백제삼계탕");
        mockItem.setAddr1("서울특별시 중구 명동8길 8-10 (명동2가)");
        mockItem.setMapx("126.9841178194");
        mockItem.setMapy("37.5634241535");
        mockItem.setAddr2("");

        List<TourApiItem> mockItems = List.of(mockItem);

        String sampleJson = TourApiTestJsonFactory.createJsonFromItems(mockItems);

        ObjectMapper objectMapper = new ObjectMapper();
        TourApiResponseDto dto = objectMapper.readValue(sampleJson, TourApiResponseDto.class);

        assertNotNull(dto.getResponse(), "response 객체 != null");
        assertNotNull(dto.getResponse().getBody(), "body 객체 != null");

        List<TourApiItem> items = dto.getResponse().getBody().getItems().getItem();
        assertNotNull(items, "item 리스트 != null");
        assertNotNull(items.isEmpty(), "item 리스트 != null");

        TourApiItem item = items.get(0);
        assertEquals("백제삼계탕", item.getTitle());
        assertEquals("서울특별시 중구 명동8길 8-10 (명동2가)", item.getAddr1());
        assertEquals("126.9841178194", item.getMapx());
        assertEquals("37.5634241535", item.getMapy());
    }
}
