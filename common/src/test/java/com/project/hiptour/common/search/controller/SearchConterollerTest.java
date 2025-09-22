package com.project.hiptour.common.search.controller;

import com.project.hiptour.common.search.Service.SearchService;
import com.project.hiptour.common.search.dto.SearchResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    @DisplayName("키워드로 장소 검색")
    void testSearchPlaces() throws Exception {
        // given
        SearchResponseDto dto = SearchResponseDto.builder()
                .placeId(1)
                .placeName("부산 해운대")
                .address1("부산광역시")
                .address2("해운대구")
                .tel("051-123-4567")
                .regionName("부산")
                .categoryName("관광지")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("placeName").ascending());
        Page<SearchResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);

        Mockito.when(searchService.searchPlaces(eq("부산"), any(Pageable.class)))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/search")
                        .param("keyword", "부산")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "placeName")
                        .param("direction", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].placeName").value("부산 해운대"))
                .andExpect(jsonPath("$.content[0].regionName").value("부산"));
    }

    @Test
    @DisplayName("찜목록 필터링")
    void testSearchHeartedPlaces() throws Exception {
        // given
        SearchResponseDto dto = SearchResponseDto.builder()
                .placeId(2)
                .placeName("서울 남산타워")
                .address1("서울특별시")
                .address2("용산구")
                .tel("02-987-6543")
                .regionName("서울")
                .categoryName("관광지")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("placeName").ascending());
        Page<SearchResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);

        Mockito.when(searchService.searchHeartedPlaces(eq("서울"), any(Pageable.class), any(String.class)))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/search/hearted")
                        .param("keyword", "서울")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "placeName")
                        .param("direction", "asc")
                        .header("Authorization", "Bearer test-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].placeName").value("서울 남산타워"))
                .andExpect(jsonPath("$.content[0].regionName").value("서울"));
    }
}