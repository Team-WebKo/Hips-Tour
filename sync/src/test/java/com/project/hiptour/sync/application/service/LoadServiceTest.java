package com.project.hiptour.sync.application.service;

import com.project.hiptour.sync.application.port.TourApiPort;
import com.project.hiptour.sync.infrastructure.persistence.LoadStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class LoadServiceTest {
    @MockitoBean
    private TourApiPort tourApiPort;

    @Autowired
    private LoadService loadService;

    @Autowired
    private LoadStatusRepository loadStatusRepository;

    @Test
    @DisplayName("모든 지역 데이터 적재가 이미 완료된 경우, 작업이 즉시 종료되어야 합니다.")
    void LoadAllPlaces_already_success_case() {
        // Given

    }
}
