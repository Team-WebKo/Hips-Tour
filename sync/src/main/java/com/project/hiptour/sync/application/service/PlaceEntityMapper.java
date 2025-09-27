package com.project.hiptour.sync.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.embedable.GeoPoint;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceEntityMapper {
    private final ObjectMapper objectMapper;

    /**
     * SyncPlaceDto를 새로운 Place 엔티티로 변환합니다.
     * @param dto TourAPI로부터 받은 데이터 DTO
     * @return 새로운 Place 엔티티
     */
    public Place mapDtoToNewEntity(SyncPlaceDto dto) {
        Place place = new Place();
        place.setContentId(dto.getContentid());
        place.setPlaceName(dto.getTitle());
        place.setAddress1(dto.getAddr1());
        place.setAddress2(dto.getAddr2());
        place.setTelNumber(dto.getTel());
        place.setImageUrl(dto.getFirstimage());
        place.setAreaCode(dto.getAreacode());
        place.setContentTypeId(dto.getContenttypeid());

        try {
            if (dto.getModifiedtime() != null && !dto.getModifiedtime().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                place.setSourceModifiedTime(LocalDateTime.parse(dto.getModifiedtime(), formatter));
            }
        } catch (Exception e) {
            place.setSourceModifiedTime(null);
        }

        try {
            double longitude = Double.parseDouble(dto.getMapx());
            double latitude = Double.parseDouble(dto.getMapy());
            int mLevel = Integer.parseInt(dto.getMlevel());
            place.setGeoPoint(new GeoPoint(latitude, longitude, mLevel));
        } catch (NumberFormatException e) {
            place.setGeoPoint(null);
        }

        return place;
    }

    /**
     * 기존 Place 엔티티의 정보를 SyncPlaceDto의 내용으로 업데이트 합니다.
     * @param place 업데이트할 기존 엔티티
     * @param dto 새로운 데이터 DTO
     */
    public void updateEntityFromDto(Place place, SyncPlaceDto dto) {
        place.setPlaceName(dto.getTitle());
        place.setAddress1(dto.getAddr1());
        place.setAddress2(dto.getAddr2());
        place.setTelNumber(dto.getTel());
        place.setImageUrl(dto.getFirstimage());
        place.setAreaCode(dto.getAreacode());
        place.setContentTypeId(dto.getContenttypeid());

        try {
            if (dto.getModifiedtime() != null && !dto.getModifiedtime().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                place.setSourceModifiedTime(LocalDateTime.parse(dto.getModifiedtime(), formatter));
            }
        } catch (Exception e) {
            place.setSourceModifiedTime(null);
        }

        try {
            double longitude = Double.parseDouble(dto.getMapx());
            double latitude = Double.parseDouble(dto.getMapy());
            int mLevel = Integer.parseInt(dto.getMlevel());
            place.setGeoPoint(new GeoPoint(latitude, longitude, mLevel));
        } catch (NumberFormatException e) {
            place.setGeoPoint(null);
        }
    }

    /**
     * TourAPI의 JSON 응답 문자열을 SyncPlaceDto 리스트로 파싱
     * @param jsonResponse API 응답
     * @return SyncPlaceDto 리스트
     */
    public List<SyncPlaceDto> parseResponseToDtoList(String jsonResponse) throws IOException {
        List<SyncPlaceDto> dtoList = new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode itemsNode = root.path("response").path("body").path("items").path("item");

        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                SyncPlaceDto dto = objectMapper.treeToValue(itemNode, SyncPlaceDto.class);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }
}
