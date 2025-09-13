package com.project.hiptour.sync.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.sync.domain.TourPlace;
import com.project.hiptour.sync.global.dto.SyncPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceEntityMapper {
    private final ObjectMapper objectMapper;

    public TourPlace mapDtoToNewEntity(SyncPlaceDto dto) {
        String fullAddress = dto.getAddr1() + (dto.getAddr2() != null && !dto.getAddr2().isEmpty() ? " " + dto.getAddr2() : "");
        return TourPlace.builder()
                .contentId(dto.getContentid())
                .title(dto.getTitle())
                .address(fullAddress)
                .areaCode(dto.getAreacode())
                .imageUrl(dto.getFirstimage())
                .build();
    }

    public void updateEntityFromDto(TourPlace tourPlace, SyncPlaceDto dto) {
        String fullAddress = dto.getAddr1() + (dto.getAddr2() != null && !dto.getAddr2().isEmpty() ? " " + dto.getAddr2() : "");
        tourPlace.setTitle(dto.getTitle());
        tourPlace.setAddress(fullAddress);
        tourPlace.setAreaCode(dto.getAreacode());
        tourPlace.setImageUrl(dto.getFirstimage());
    }

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
