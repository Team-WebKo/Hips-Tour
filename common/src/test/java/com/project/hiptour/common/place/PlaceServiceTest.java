package com.project.hiptour.common.place;

import com.project.hiptour.common.place.controller.PlaceController;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceController.class)
public class PlaceServiceTest {
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
}