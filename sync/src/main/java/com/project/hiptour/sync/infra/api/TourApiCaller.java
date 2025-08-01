package com.project.hiptour.sync.infra.api;

import com.project.hiptour.sync.config.TourApiProperties;
import com.project.hiptour.common.place.PlaceDto;
import com.project.hiptour.sync.dto.TourApiDto;
import com.project.hiptour.sync.dto.TourApiItem;
import com.project.hiptour.sync.external.api.TourDataApiCaller;
import com.project.hiptour.sync.infra.mapper.TourApiDtoMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TourApiCaller implements TourDataApiCaller {
    private final RestTemplate restTemplate;
    private final TourApiProperties properties;
    private final TourApiDtoMapper dtoMapper;
    private final String serviceKey = "서비스키_입력_필요"; //서비스 키 보안 생각하자

    public TourApiCaller(RestTemplateBuilder builder, TourApiProperties properties, TourApiDtoMapper dtoMapper) {
        this.restTemplate = builder.build();
        this.properties = properties;
        this.dtoMapper = dtoMapper;
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

    @Override
    public List<PlaceDto> fetchChangedSince(LocalDateTime lastSuccessTime) {
        String formattedTime = lastSuccessTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        Map<String, String> paramMap = Map.of(
                "serviceKey", properties.getServiceKey(),
                "numOfRows", String.valueOf(properties.getDefaultRows()),
                "pageNo", String.valueOf(properties.getDefaultPage()),
                "MobileOS", properties.getMobileOs(),
                "MobileApp", properties.getMobileApp(),
                "modifiedtime", formattedTime,
                "_type", properties.getType()
        );

        String url = ParamsBuilder.toUrl(properties.getHost(), paramMap);
        String response = restTemplate.getForObject(url, String.class);

        List<TourApiItem> items = dtoMapper.toItemList(response);
        List<TourApiDto> dtos = dtoMapper.toDtoList(items);

        return dtos.stream()
                .map(dto -> new PlaceDto(
                        dto.getPlaceName(),
                        dto.getAddress1(),
                        dto.getAddress2(),
                        dto.getLatitude(),
                        dto.getLongitude()))
                .collect(Collectors.toList());
    }
}
