package com.project.hiptour.common.web.search;

import com.project.hiptour.common.entity.place.Place;
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
//    private boolean hearted;
    public SearchResponseDto(Place place) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.address1 = place.getAddress1();
        this.address2 = place.getAddress2();
        this.tel = place.getTelNumber() != null ? place.getTelNumber().toString() : null;
        this.regionName = place.getRegionInfo() != null ? place.getRegionInfo().getRegionName() : null;
//        this.categoryName = place.getCategory() != null ? place.getCategory().getCategoryName() : null;
//        this.hearted = hearted;
    }
}
