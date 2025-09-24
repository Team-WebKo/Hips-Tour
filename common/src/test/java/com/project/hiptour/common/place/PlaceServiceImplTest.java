package com.project.hiptour.common.place;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.exception.place.PlaceNotFoundException;
import com.project.hiptour.common.usercase.place.PlaceServiceImpl;
import com.project.hiptour.common.util.PageResponseDto;
import com.project.hiptour.common.web.place.PlaceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceImplTest {
    @InjectMocks
    private PlaceServiceImpl placeService;

    @Mock
    private PlaceRepository placeRepository;

    private Place testPlace1;
    private Place testPlace2;
    private Place testPlace3;

    @BeforeEach
    void init() {
        GeoPoint geoPoint1 = new GeoPoint(37.5665, 126.9780);
        testPlace1 = Place.builder()
                .placeName("테스트 장소 1")
                .address1("주소1")
                .address2("상세주소1")
                .geoPoint(geoPoint1)
                .telNumber("02-1234-5678")
                .build();
        testPlace1.setPlaceId(1);
        testPlace1.setCreatedAt(LocalDateTime.now().minusDays(1));

        GeoPoint geoPoint2 = new GeoPoint(33.4507, 126.5706);
        testPlace2 = Place.builder()
                .placeName("테스트 장소 2")
                .address1("주소2")
                .address2("상세주소2")
                .geoPoint(geoPoint2)
                .telNumber("064-9876-5432")
                .build();
        testPlace2.setPlaceId(2);
        testPlace2.setCreatedAt(LocalDateTime.now());

        GeoPoint geoPoint3 = new GeoPoint(34.0522, 118.2437);
        testPlace3 = Place.builder()
                .placeName("테스트 장소 3")
                .address1("주소3")
                .address2("상세주소3")
                .geoPoint(geoPoint3)
                .telNumber("070-1111-2222")
                .build();
        testPlace3.setPlaceId(3);
        testPlace3.setCreatedAt(LocalDateTime.now().minusDays(2));
    }

    @Nested
    @DisplayName("장소 상세 조회 (findPlace)")
    class FindPlace {
        @Test
        @DisplayName("성공 - 장소 반환")
        void findPlace_success() {
            when(placeRepository.findById(1)).thenReturn(Optional.of(testPlace1));

            PlaceDto result = placeService.findPlace(1);

            assertThat(result).isNotNull();
            assertThat(result.getPlaceId()).isEqualTo(1);
            assertThat(result.getPlaceName()).isEqualTo("테스트 장소 1");
        }

        @Test
        @DisplayName("실패 - 장소 없음")
        void findPlace_fail_notFound() {
            when(placeRepository.findById(anyInt())).thenReturn(Optional.empty());

            assertThrows(PlaceNotFoundException.class, () -> placeService.findPlace(999));
        }
    }

    @Nested
    @DisplayName("추천 장소 조회 (findRecommendedPlaces)")
    class FindRecommendedPlaces {
        @Test
        @DisplayName("성공 - 최근 등록된 장소 순으로 반환")
        void findRecommendedPlaces_success() {
            Pageable pageable = PageRequest.of(0, 10);
            List<Place> recentPlaces = Arrays.asList(testPlace2, testPlace1, testPlace3);
            Page<Place> recentPage = new PageImpl<>(recentPlaces, pageable, recentPlaces.size());

            when(placeRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(recentPage);

            PageResponseDto<PlaceDto> result = placeService.findRecommendedPlaces(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(3);
            assertThat(result.getContent().get(0).getPlaceId()).isEqualTo(testPlace2.getPlaceId());
        }

        @Test
        @DisplayName("결과 없음 - 장소가 하나도 없는 경우 빈 페이지 반환")
        void findRecommendedPlaces_empty() {
            Pageable pageable = PageRequest.of(0, 10);
            when(placeRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(Page.empty(pageable));

            PageResponseDto<PlaceDto> result = placeService.findRecommendedPlaces(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent().isEmpty()).isTrue();
        }
    }
}