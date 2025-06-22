package com.project.hiptour.sync.infra.api;

import com.project.hiptour.sync.config.TourApiProperties;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TourApiCaller implements TourDataApiCaller {
    private final RestTemplate restTemplate;
    private final TourApiProperties properties;
    private final String serviceKey = "서비스키_입력_필요"; //서비스 키 보안 생각하자

    public TourApiCaller(RestTemplateBuilder builder, TourApiProperties properties) {
        this.restTemplate = builder.build();
        this.properties = properties;
    }

    @Override
    public String fetchPlaceData(int areaCode) {
        Map<String, String> paramMap = Map.of(
                "serviceKey", properties.getServiceKey(),
                "numOfRows", String.valueOf(properties.getDefaultRows()),
                "pageNo", String.valueOf(properties.getDefaultPage()),
                "MobilesOs", properties.getMobileOs(),
                "MobileApp", properties.getMobileApp(),
                "areaCode", String.valueOf(areaCode),
                "_type", properties.getType()
        );

        String url = ParamsBuilder.toUrl(properties.getHost(), paramMap);
        return restTemplate.getForObject(url, String.class);
    }
}