package com.project.hiptour.sync.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tourapi")
@Getter
@Setter
public class TourApiProperties {
    private String baseUrl;
    private String serviceKey;
    private String mobileOS;
    private String mobileApp;
    private String type;
    private int numOfRows;
}
