package com.project.hiptour.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceDto {
    private String name;
    private String address1;
    private String address2;
    private double latitude;
    private double longitude;
}