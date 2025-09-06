package com.project.hiptour.common.search.controller;

import com.project.hiptour.common.search.Service.SearchService;
import com.project.hiptour.common.search.dto.SearchResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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

    //review에 공용 config 만들어놨음, 추후 병합 후 마이그레이션 할 예정
    //현재 보이는 deprecated는 실행 시 문제없음
    @MockBean
    private SearchService searchService;

    @Test
    void searchPlaces() throws Exception {
        // given
        SearchResponseDto dto = SearchResponseDto.builder()
                .placeId(1)
                .placeName("서울타워")
                .address1("서울시 중구")
                .address2("남산공원")
                .tel("02-123-4567")
                .regionName("서울")
                .categoryName("관광지")
                .build();

        Page<SearchResponseDto> page = new PageImpl<>(List.of(dto),
                PageRequest.of(0, 10, Sort.by("placeName")), 1);

        Mockito.when(searchService.searchPlaces(eq("서울"), any(Pageable.class)))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/Search/search")
                        .param("keyword", "서울")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "placeName")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].placeName").value("서울타워"))
                .andExpect(jsonPath("$.content[0].categoryName").value("관광지"));
    }
}