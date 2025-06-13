package com.project.hiptour.sync.infra.api;

import com.project.hiptour.sync.external.api.TourDataApiCaller;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TourApiCaller implements TourDataApiCaller {
    private final RestTemplate restTemplate;
    private final String serviceKey = "서비스키_입력_필요"; //서비스 키 보안 생각하자

    public TourApiCaller(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public String fetchPlaceData(int areaCode) {
        String url = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1"
                + "?serviceKey=" + serviceKey
                + "&numOfRows=100"
                + "&pageNo=1"
                + "&MobileOS=ETC"
                + "&MobileApp=Hipstour" //이름 변경 필요?
                + "&areaCode=" + areaCode
                + "&_type=json";

        return restTemplate.getForObject(url, String.class);
    }
}