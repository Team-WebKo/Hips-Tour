package com.project.hiptour.common.place.service;

import com.project.hiptour.common.entity.place.Category;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.embedable.TelNumber;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.repository.PlaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceUnitTest {
    @InjectMocks
    private PlaceService placeService;

    @Mock
    private PlaceRepository placeRepository;

    @Test
    @DisplayName("특정 카테고리 Id로 장소 목록 조회 - 성공")
    void findPlacesByCategoryId_Success() {
        int categoryId = 1;
        Category mockCategory = new Category(categoryId, "테스트 카테고리", "");
        Place place1 = new Place("장소1", "주소1", "상세주소1", new GeoPoint(), new TelNumber(""));
        place1.setCategory(mockCategory);
        Place place2 = new Place("장소2", "주소2", "상세주소2", new GeoPoint(), new TelNumber(""));
        place2.setCategory(mockCategory);

        List<Place> mockPlaces = List.of(place1, place2);
        given(placeRepository.findByCategoryCategoryId(categoryId)).willReturn(mockPlaces);

        List< PlaceDto> result = placeService.findPlacesByCategoryId(categoryId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPlaceName()).isEqualTo("장소1");
        assertThat(result.get(1).getPlaceName()).isEqualTo("장소2");
    }
}
