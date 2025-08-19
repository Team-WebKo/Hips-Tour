package com.project.hiptour.common.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchResponseDto {
    private Integer placeId;
    private String placeName;
    private String address1;
    private String address2;
    private String tel;
    private String regionName;
    private String categoryName;
}
