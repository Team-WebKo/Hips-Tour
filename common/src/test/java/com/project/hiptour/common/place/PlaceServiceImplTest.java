package com.project.hiptour.common.place;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.common.entity.place.embedable.TelNumber;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.serviceImpl.PlaceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.*;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
        TelNumber telNumber1 = new TelNumber("02-1234-5678");
        testPlace1 = Place.builder()
                .placeName("테스트 장소 1")
                .address1("주소1")
                .address2("상세주소1")
                .geoPoint(geoPoint1)
                .telNumber(telNumber1)
                .build();
        testPlace1.setPlaceId(1);
        testPlace1.setCreatedAt(LocalDateTime.now().minusDays(1));

        GeoPoint geoPoint2 = new GeoPoint(33.4507, 126.5706);
        TelNumber telNumber2 = new TelNumber("064-9876-5432");
        testPlace2 = Place.builder()
                .placeName("테스트 장소 2")
                .address1("주소2")
                .address2("상세주소2")
                .geoPoint(geoPoint2)
                .telNumber(telNumber2)
                .build();
        testPlace2.setPlaceId(2);
        testPlace2.setCreatedAt(LocalDateTime.now());

        GeoPoint geoPoint3 = new GeoPoint(34.0522, 118.2437);
        TelNumber telNumber3 = new TelNumber("070-1111-2222");
        testPlace3 = Place.builder()
                .placeName("테스트 장소 3")
                .address1("주소3")
                .address2("상세주소3")
                .geoPoint(geoPoint3)
                .telNumber(telNumber3)
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

            assertThrows(EntityNotFoundException.class, () -> placeService.findPlace(999));
        }
    }

    @Nested
    @DisplayName("추천 장소 조회 (findRecommendedPlaces)")
    class FindRecommendedPlaces {
        @Test
        @DisplayName("성공 - 찜 개수 기반 추천 (찜이 있는 경우)")
        void findRecommendedPlaces_success_heartCount() {
            Pageable pageable = PageRequest.of(0, 10);
            List<Place> heartedPlaces = Arrays.asList(testPlace1, testPlace2);
            Page<Place> heartedPage = new PageImpl<>(heartedPlaces, pageable, heartedPlaces.size());

            when(placeRepository.findPlacesOrderByHeartCount(pageable)).thenReturn(heartedPage);
            when(placeRepository.findByPlaceIdNotInOrderByCreatedAtDesc(any(Collection.class), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

            Page<PlaceDto> result = placeService.findRecommendedPlaces(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getPlaceId()).isEqualTo(testPlace1.getPlaceId());
        }

        @Test
        @DisplayName("성공 - 콜드 스타트 (찜이 0인 경우)")
        void findRecommendedPlaces_success_coldStart() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Place> emptyHeartedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
            List<Place> recentPlaces = Arrays.asList(testPlace2, testPlace1);
            Page<Place> recentPage = new PageImpl<>(recentPlaces, pageable, recentPlaces.size());

            when(placeRepository.findPlacesOrderByHeartCount(pageable)).thenReturn(emptyHeartedPage);
            when(placeRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(recentPage);

            Page<PlaceDto> result = placeService.findRecommendedPlaces(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getPlaceId()).isEqualTo(testPlace2.getPlaceId());
        }

        @Test
        @DisplayName("성공 - 찜 개수가 부족할 경우 최근 등록순으로 반환")
        void findRecommendedPlaces_success_heartedPlacesAndRecentPlaces() {
            Pageable pageable = PageRequest.of(0, 2);
            List<Place> heartedPlaces = Collections.singletonList(testPlace1);
            Page<Place> partialHeartedPage = new PageImpl<>(heartedPlaces, pageable, 10);

            List<Place> recentPlacesToFill = Collections.singletonList(testPlace2);
            Page<Place> recentPage = new PageImpl<>(recentPlacesToFill, PageRequest.of(0, 1), 1);

            when(placeRepository.findPlacesOrderByHeartCount(pageable)).thenReturn(partialHeartedPage);
            when(placeRepository.findByPlaceIdNotInOrderByCreatedAtDesc(any(Collection.class), any(Pageable.class))).thenReturn(recentPage);

            Page<PlaceDto> result = placeService.findRecommendedPlaces(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getPlaceId()).isEqualTo(testPlace1.getPlaceId());
            assertThat(result.getContent().get(1).getPlaceId()).isEqualTo(testPlace2.getPlaceId());
        }

        @Test
        @DisplayName("성공 - 찜도 없고 최근 추가도 없는 경우 (여행지 자체가 비어있는 경우 - 실패 가정도 일치)")
        void findRecommendedPlaces_success_emptyAll() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Place> emptyHeartedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
            Page<Place> emptyRecentPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(placeRepository.findPlacesOrderByHeartCount(pageable)).thenReturn(emptyHeartedPage);
            when(placeRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(emptyRecentPage);

            Page<PlaceDto> result = placeService.findRecommendedPlaces(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }
}