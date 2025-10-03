package com.project.hiptour.sync.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.sync.application.port.TourApiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverviewFillService {
    private final PlaceRepository placeRepository;
    private final TourApiPort tourApiPort;
    private final ObjectMapper objectMapper;

    /**
     * API 호출 가능 범위 내에서 overview가 비어있는 장소들의 상세 정보를 채워넣습니다.
     * @param apiCallBudget 이 작업에서 사용할 수 있는 최대 API 호출 횟수
     */
    @Transactional
    public void fillMissingOverviews(int apiCallBudget) {
        if (apiCallBudget <= 0) {
            log.info("API 호출이 불가능합니다. 작업을 건너뜁니다.");
            return;
        }

        log.info("여행지 개요 추가 작업을 시작합니다. API CALL REMAIN: {}", apiCallBudget);

        Page< Place> placesToUpdatePage = placeRepository.findByOverviewIsNull(PageRequest.of(0, apiCallBudget));
        List<Place> placesToUpdate = placesToUpdatePage.getContent();

        if (placesToUpdate.isEmpty()) {
            log.info("모든 여행지 개요가 추가되었습니다. 작업을 종료합니다.");
            return;
        }

        log.info("여행지 개요를 추가하였습니다. (Total: {})", placesToUpdate.size());

        for (Place place : placesToUpdate) {
            try {
                String jsonResponse = tourApiPort.fetchDetail(place.getContentId());
                if (jsonResponse != null) {
                    String overview = parseOverviewFromJson(jsonResponse);
                    if (overview != null && !overview.isEmpty()) {
                        place.setOverview(overview);
                    }
                }
            } catch (Exception e) {
                log.error("'contentId: {}'의 여행지 정보를 불러오는데 실패했습니다.", place.getContentId(), e);
            }
        }

        log.info("여행지 정보 불러오기 작업이 끝났습니다. (Total: {})", placesToUpdate.size());
    }

    private String parseOverviewFromJson(String jsonResponse) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode itemNode = root.path("response").path("body").path("items").path("item");

        if (itemNode.isArray() && !itemNode.isEmpty()) {
            itemNode = itemNode.get(0);
        }

        if (itemNode.has("overview")) {
            return itemNode.get("overview").asText();
        }

        return null;
    }
}
