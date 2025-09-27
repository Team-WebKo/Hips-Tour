package com.project.hiptour.common.entity.place;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    TOURIST_ATTRACTION("12", "관광지"),
    CULTURAL_FACILITIES("14", "문화시설"),
    FESTIVAL("15", "행사/공연/축제"),
    TRAVEL_COURSE("25", "여행코스"),
    LEPORTS("28", "레포츠"),
    ACCOMMODATION("32", "숙박"),
    SHOPPING("38", "쇼핑"),
    RESTAURANT("39", "음식점");

    private final String code;
    private final String description;

    @JsonCreator
    public static ContentType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        return Arrays.stream(ContentType.values())
                .filter(contentType -> Objects.equals(contentType.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
