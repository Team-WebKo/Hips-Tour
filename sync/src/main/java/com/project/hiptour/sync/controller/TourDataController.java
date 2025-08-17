package com.project.hiptour.sync.controller;

import com.project.hiptour.sync.application.service.LoadService;
import com.project.hiptour.sync.application.service.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/tour/data")
@RequiredArgsConstructor
public class TourDataController {
    private final LoadService loadService;
    private final SyncService syncService;

    @PostMapping("/load-all")
    public ResponseEntity<String> loadAllPlaces() {
        loadService.loadAllPlaces();
        return ResponseEntity.ok("TourAPI 데이터 적재");
    }

    @PostMapping("/sync-updated")
    public ResponseEntity<String> syncUpdatedPlaces(LocalDateTime time) {
        syncService.syncUpdatedPlaces(time);
        return ResponseEntity.ok("TourAPI 변경 데이터 동기화");
    }
}