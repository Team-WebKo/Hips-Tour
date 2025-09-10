package com.project.hiptour.common.place;

import com.project.hiptour.common.place.controller.PlaceController;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceController.class)
public class PlaceConterollerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceService placeService;

    @Test
    @DisplayName("여행지 검색에 대한 요청에 올바르게 작동합니다.")
    void getPlace() throws Exception {
        // Given
        Integer placeId = 1;
        PlaceDto fakeDto = PlaceDto.builder().placeId(placeId).placeName("Fake 여행지").build();
        given(placeService.findPlace(placeId)).willReturn(fakeDto);

        // When & Then: 컨트롤러에 요청을 보내고, 약속된 응답이 오는지 확인한다.
        mockMvc.perform(get("/api/places/{id}", placeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placeId").value(1))
                .andExpect(jsonPath("$.placeName").value("Fake 여행지"));
    }

    @Test
    @DisplayName("카테고리별 여행지 목록 조회 API - 성공")
    void getPlacesByCategoryApi_Success() throws Exception {
        int categoryId = 1;
        PlaceDto dto1 = PlaceDto.builder().placeId(10).placeName("장소10").build();
        PlaceDto dto2 = PlaceDto.builder().placeId(20).placeName("장소20").build();
        List<PlaceDto> mockDtos = List.of(dto1, dto2);

        given(placeService.findPlacesByCategoryId(categoryId)).willReturn(mockDtos);

        mockMvc.perform(get("/api/places").param("categoryId", String.valueOf(categoryId)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].placeName").value("장소10"))
                .andExpect(jsonPath("$[1].placeName").value("장소20"))
                .andDo(print());
    }
}