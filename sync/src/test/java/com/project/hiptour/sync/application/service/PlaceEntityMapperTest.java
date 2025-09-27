package com.project.hiptour.sync.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaceEntityMapperTest {

    private PlaceEntityMapper placeEntityMapper;

    @BeforeEach
    void init() {
        placeEntityMapper = new PlaceEntityMapper(new ObjectMapper());
    }

    private SyncPlaceDto createValidSyncPlaceDto() {
        return SyncPlaceDto.builder()
                .contentid("12345")
                .contenttypeid("12")
                .title("테스트 장소")
                .addr1("테스트 주소 1")
                .addr2("상세주소")
                .tel("02-1234-5678")
                .firstimage("http://example.com/image.jpg")
                .areacode("1")
                .mapx("127.001")
                .mapy("37.002")
                .mlevel("6")
                .modifiedtime("20250925100000")
                .build();
    }

    @Nested
    @DisplayName("mapDtoToNewEntity 메서드는")
    class Describe_mapDtoToNewEntity {
        @Test
        @DisplayName("정상적인 DTO를 받으면 모든 필드가 매핑된 새로운 Place 엔티티를 반환합니다.")
        void returns_new_place_entity_with_all_fields_mapped() {
            SyncPlaceDto dto = createValidSyncPlaceDto();

            Place result = placeEntityMapper.mapDtoToNewEntity(dto);

            assertThat(result).isNotNull();
            assertThat(result.getContentId()).isEqualTo("12345");
            assertThat(result.getContentTypeId()).isEqualTo("12");
            assertThat(result.getPlaceName()).isEqualTo("테스트 장소");
            assertThat(result.getAddress1()).isEqualTo("테스트 주소 1");
            assertThat(result.getAreaCode()).isEqualTo("1");
            assertThat(result.getImageUrl()).isEqualTo("http://example.com/image.jpg");
            assertThat(result.getTelNumber()).isEqualTo("02-1234-5678");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime expectedTime = LocalDateTime.parse("20250925100000", formatter);
            assertThat(result.getSourceModifiedTime()).isEqualTo(expectedTime);

            assertThat(result.getGeoPoint()).isNotNull();
            assertThat(result.getGeoPoint().getLatitude()).isEqualTo(37.002);
            assertThat(result.getGeoPoint().getLongitude()).isEqualTo(127.001);
            assertThat(result.getGeoPoint().getMLevel()).isEqualTo(6);
        }
    }
}
