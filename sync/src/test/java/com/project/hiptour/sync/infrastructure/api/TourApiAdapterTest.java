package com.project.hiptour.sync.infrastructure.api;

import com.project.hiptour.common.CommonApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class TourApiAdapterTest {
    @Autowired
    private TourApiAdapter tourApiAdapter;

    @Test
    @DisplayName("API 응답 형식이 올바르게 동작합니다.")
    void fetchPlaceData_shouldReturnApiResponse() {
        // given
        int pageNo = 1;
        int numOfRows = 10;

        // when
        String response = tourApiAdapter.fetchPlaceData(pageNo, numOfRows);

        // then
        assertNotNull(response, "API 응답은 'null'이 아니어야 합니다.");
        assertTrue(response.contains("response"), "응답에 'response' 키워드가 존재해야 합니다 (API 응답 형식 확인).");
        System.out.println("TourAPI 응답:\n" + response);
    }
}