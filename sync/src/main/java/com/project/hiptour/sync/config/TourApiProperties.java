package com.project.hiptour.sync.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tourapi")
public class TourApiProperties {
    private String host;
    private String serviceKey;
    private String mobileOs;
    private String mobileApp;
    private int defaultRows;
    private int defaultPage;
    private String type;
}
