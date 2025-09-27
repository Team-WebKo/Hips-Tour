package com.project.hiptour.common.entity.place;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum AreaCode {
    SEOUL("1", "서울"),
    INCHEON("2", "인천"),
    DAEJEON("3", "대전"),
    DAEGU("4", "대구"),
    GWANGJU("5", "광주"),
    BUSAN("6", "부산"),
    ULSAN("7", "울산"),
    SEJONG("8", "세종"),
    GYEONGGI("31", "경기"),
    GANGWON("32", "강원"),
    CHUNGBUK("33", "충북"),
    CHUNGNAM("34", "충남"),
    GYEONGBUK("35", "경북"),
    GYEONGNAM("36", "경남"),
    JEONBUK("37", "전북"),
    JEONNAM("38", "전남"),
    JEJU("39", "제주");

    private final String code;
    private final String name;

    @JsonCreator
    public static AreaCode fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }

        return Arrays.stream(AreaCode.values())
                .filter(areaCode -> Objects.equals(areaCode.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
