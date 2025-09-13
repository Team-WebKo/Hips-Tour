//package com.project.hiptour.common.search.service;
//
//
//import com.project.hiptour.common.entity.place.Category;
//import com.project.hiptour.common.entity.place.Place;
//import com.project.hiptour.common.entity.place.RegionInfo;
//import com.project.hiptour.common.entity.place.embedable.GeoPoint;
//import com.project.hiptour.common.entity.place.embedable.TelNumber;
//import com.project.hiptour.common.search.dto.SearchResponseDto;
//import com.project.hiptour.common.search.repository.SearchRepository;
//import com.project.hiptour.common.search.serviceImpl.SearchServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class SearchServiceImplTest {
//
//    @Mock
//    private SearchRepository searchRepository;
//
//    @InjectMocks
//    private SearchServiceImpl searchService;
//
//    @Test
//    void 장소검색_성공시() {
//        // given
//        Category category = new Category();
//        category.setCategoryName("관광지");
//
//        RegionInfo region = new RegionInfo();
//
//        Place place1 = new Place("서울타워", "서울시 중구", "남산공원",
//                new GeoPoint(37.551169, 126.988227), new TelNumber("02-123-4567"));
//        place1.setPlaceId(1);
////        place1.setCategory(category);
//        place1.setRegionInfo(region);
//
//        Place place2 = new Place("부산 해운대", "부산시 해운대구", "해운대 해수욕장",
//                new GeoPoint(35.158698, 129.160384), new TelNumber("051-987-6543"));
//        place2.setPlaceId(2);
////        place2.setCategory(category);
//
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("placeName"));
//        Page<Place> mockPage = new PageImpl<>(List.of(place1, place2), pageable, 2);
//
//        given(searchRepository.findByPlaceNameContaining("서울", pageable))
//                .willReturn(new PageImpl<>(List.of(place1), pageable, 1));
//
//        // when
//        Page<SearchResponseDto> result = searchService.searchPlaces("서울", pageable);
//
//        // then
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        assertThat(result.getContent().get(0).getPlaceName()).isEqualTo("서울타워");
//        assertThat(result.getContent().get(0).getCategoryName()).isEqualTo("관광지");
//    }
//
//    @Test
//    void 없는키워드_빈페이지() {
//        // given
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("placeName"));
//        given(searchRepository.findByPlaceNameContaining("없는키워드", pageable))
//                .willReturn(Page.empty(pageable));
//
//        // when
//        Page<SearchResponseDto> result = searchService.searchPlaces("없는키워드", pageable);
//
//        // then
//        assertThat(result.getTotalElements()).isZero();
//        assertThat(result.getContent()).isEmpty();
//        verify(searchRepository).findByPlaceNameContaining("없는키워드", pageable);
//    }
//
//}