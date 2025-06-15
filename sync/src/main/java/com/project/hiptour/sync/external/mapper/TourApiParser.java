package com.project.hiptour.sync.external.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.dto.TourApiResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TourApiParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TourApiResponseDto parse(String json) {
        try {
            return objectMapper.readValue(json, TourApiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("TourAPI 파싱 실패", e);
        }
    }

    public List<TourApiDto> convertToDtoList(List<TourApiItem> items) {
        List<TourApiDto> result = new ArrayList<>();
        for (TourApiItem item : items) {
            TourApiDto dto = new TourApiDto(
                    item.getTitle(),
                    item.getAddr1(),
                    item.getAddr2(),
                    item.getMapx(),
                    item.getMapy()
            );

            result.add(dto);
        }

        return result;
    }
}
