package com.project.hiptour.sync.application.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class TourApiTestJsonFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String createJsonFromItems(List<?> items) {
        Map<String, Object> jsonString = Map.of(
                "response", Map.of(
                        "header", Map.of(
                                "resultCode", "0000",
                                "resultMsg", "OK"
                        ),
                        "body", Map.of(
                                "items", Map.of(
                                        "item", items
                                )
                        )
                )
        );

        try {
            return objectMapper.writeValueAsString(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 생성 실패", e);
        }
    }
}
