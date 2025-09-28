package com.project.hiptour.sync.application.service;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.RegionInfo;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.entity.place.repos.RegionInfoRepository;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaceMapperServiceTest {
    @Mock
    private PlaceEntityMapper placeEntityMapper;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private RegionInfoRepository regionInfoRepository;

    @InjectMocks
    private PlaceMapperService placeMapperService;

    private SyncPlaceDto createValidSyncPlaceDto() {
        return SyncPlaceDto.builder()
                .contentid("12345")
                .areacode("1")
                .build();
    }

    @Nested
    @DisplayName("mapToEntity 메서드")
    class Describe_mapToEntity {
        @Test
        @DisplayName("DB에 해당 contentId가 없으면, 새로운 엔티티를 생성한다")
        void when_contentId_not_exists_creates_new_entity() {
            SyncPlaceDto dto = createValidSyncPlaceDto();
            Place newPlace = new Place();

            when(placeRepository.findByContentId(dto.getContentid())).thenReturn(Optional.empty());
            when(placeEntityMapper.mapDtoToNewEntity(dto)).thenReturn(newPlace);
            when(regionInfoRepository.findByAreaCode(dto.getAreacode())).thenReturn(Optional.empty());

            Place result = placeMapperService.mapToEntity(dto);

            assertThat(result).isSameAs(newPlace);

            verify(placeRepository, times(1)).findByContentId(dto.getContentid());
            verify(placeEntityMapper, times(1)).mapDtoToNewEntity(dto);
            verify(placeEntityMapper, never()).updateEntityFromDto(any(), any());
        }

        @Test
        @DisplayName("DB에 해당 contentId가 있으면, 기존 엔티티를 업데이트합니다.")
        void when_contentId_exists_updates_existing_entity() {
            SyncPlaceDto dto = createValidSyncPlaceDto();
            Place existingPlace = new Place();
            RegionInfo regionInfo = new RegionInfo();

            when(placeRepository.findByContentId(dto.getContentid())).thenReturn(Optional.of(existingPlace));
            when(regionInfoRepository.findByAreaCode(dto.getAreacode())).thenReturn(Optional.of(regionInfo));

            Place result = placeMapperService.mapToEntity(dto);

            assertThat(result).isSameAs(existingPlace);
            assertThat(result.getRegionInfo()).isSameAs(regionInfo);

            verify(placeRepository, times(1)).findByContentId(dto.getContentid());
            verify(placeEntityMapper, times(1)).updateEntityFromDto(existingPlace, dto);
            verify(placeEntityMapper, never()).mapDtoToNewEntity(any());
        }
    }
}
