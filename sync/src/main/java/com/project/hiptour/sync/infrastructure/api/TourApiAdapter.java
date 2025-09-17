package com.project.hiptour.sync.infrastructure.api;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.global.config.TourApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourApiAdapter implements TourApiPort {
    private final RestTemplate restTemplate;
    private final TourApiProperties tourApiProperties;

    @Override
    public String fetchPlaceData(int pageNo, int numOfRows, String areaCode) {
        String url = createBaseUriBuilder("/areaBasedList2")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("arrange", "A")
                .queryParam("areaCode", areaCode)
                .build(true)
                .toUriString();
//        String url = UriComponentsBuilder.fromHttpUrl(tourApiProperties.getBaseUrl() + "/areaBasedList2")
//                .queryParam("serviceKey", tourApiProperties.getServiceKey())
//                .queryParam("MobileOS", tourApiProperties.getMobileOS())
//                .queryParam("MobileApp", tourApiProperties.getMobileApp())
//                .queryParam("_type", tourApiProperties.getType())
//                .queryParam("pageNo", pageNo)
//                .queryParam("numOfRows", numOfRows)
//                .queryParam("arrange", "A")
//                .queryParam("areaCode", areaCode)
//                 TODO: 필요한 파라미터 추가 (변경 시)
//                .build(true)
//                .toUriString();

        log.info("호출하는 TourAPI URL: {}", url);

        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("TourAPI로부터 전체 데이터를 불러오는데 실패했습니다. URL: {}", url, e);
            throw new TourApiCommuniciationException("TourAPI 전체 데이터 조회 실패", e);
        }
    }

    @Override
    public String fetchChangedPlaces(LocalDateTime lastSuccessTime, int pageNo, int numOfRows) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String modifiedTime = lastSuccessTime.format(formatter);

        String url = createBaseUriBuilder("/areaBasedList2")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("arrange", "C")
                .queryParam("modifiedtime", modifiedTime)
                .build(true)
                .toUriString();

        log.info("호출하는 TourAPI URL: {}", url);

        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("TourAPI 로부터 데이터를 불러오는데 실패했습니다. URL: {}, Error: {}", url, e.getMessage());
            throw new TourApiCommunicationException("TourAPI 변경 데이터 조회 실패", e);
        }
    }

    private UriComponentsBuilder createBaseUriBuilder(String path) {
        return UriComponentsBuilder.fromHttpUrl(tourApiProperties.getBaseUrl() + path)
                .queryParam("serviceKey", tourApiProperties.getServiceKey())
                .queryParam("MobilesOs", tourApiProperties.getMobileOS())
                .queryParam("MobileApp", tourApiProperties.getMobileApp())
                .queryParam("_type", "json");
    }
}