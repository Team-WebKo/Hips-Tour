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
import java.util.stream.Collectors;

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
        return dtoList.stream()
                .map(this::mapDtoToEntity)
                .collect(Collectors.toList());
    }

    public TourPlace mapDtoToEntity(SyncPlaceDto dto) {
        String fullAddress = dto.getAddr1() + (dto.getAddr2() != null && !dto.getAddr2().isEmpty() ? " " + dto.getAddr2() : "");
        return TourPlace.builder()
                .id(dto.getContentid())
                .title(dto.getTitle())
                .address(fullAddress)
                .imageUrl(dto.getFirstimage())
                .build();
    }
}
