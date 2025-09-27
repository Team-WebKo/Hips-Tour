package com.project.hiptour.sync.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncPlaceDto {
    @JsonProperty("addr1")
    private String addr1;

    @JsonProperty("addr2")
    private String addr2;

    @JsonProperty("areacode")
    private String areacode;

    @JsonProperty("contentid")
    private String contentid;

    @JsonProperty("contenttypeid")
    private String contenttypeid;

    @JsonProperty("firstimage")
    private String firstimage;

    @JsonProperty("firstimage2")
    private String firstimage2;

    @JsonProperty("modifiedtime")
    private String modifiedtime;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("title")
    private String title;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("mapx")
    private String mapx;

    @JsonProperty("mapy")
    private String mapy;

    @JsonProperty("mlevel")
    private String mlevel;

}
