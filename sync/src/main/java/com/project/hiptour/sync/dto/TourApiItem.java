package com.project.hiptour.sync.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TourApiItem {
    private String title;
    private String addr1;
    private String addr2;
    private double mapx;
    private double mapy;
}
