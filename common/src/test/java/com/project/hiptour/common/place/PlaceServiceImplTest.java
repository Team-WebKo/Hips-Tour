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

import java.time.LocalDateTime;
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
        testPlace1.setCreatedAt(LocalDateTime.now());

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
}
