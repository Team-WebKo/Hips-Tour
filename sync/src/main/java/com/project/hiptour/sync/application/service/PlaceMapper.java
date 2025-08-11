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
public class PlaceMapper {
    private final ObjectMapper objectMapper;

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

    public List<TourPlace> mapDtoListToEntityList(List<SyncPlaceDto> dtoList) {
        List<TourPlace> entityList = new ArrayList<>();

        for (SyncPlaceDto dto : dtoList) {
            String fullAddress = dto.getAddr1() + (dto.getAddr2() != null && !dto.getAddr2().isEmpty() ? " " + dto.getAddr2() : "");
            TourPlace entity = TourPlace.builder()
                    .id(dto.getContentid())
                    .title(dto.getTitle())
                    .address(fullAddress)
                    .contentTypeId(null)
                    .imageUrl(dto.getFirstimage())
                    .description(null)
                    .build();

            entityList.add(entity);
        }

        return entityList;
    }
}
