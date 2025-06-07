package com.project.hiptour.common.command.tour;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TourApiClient {

    private final RestTemplate restTemplate;

    @Value("${tourapi.serviceKey}")
    private String serviceKey;

    public TourApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public String fetchAreaCodeJson(int pageNo, int numOfRows) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/areaCode1")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("_type", "json")
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
