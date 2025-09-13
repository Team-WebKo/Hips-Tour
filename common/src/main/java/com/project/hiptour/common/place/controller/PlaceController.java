package com.project.hiptour.common.place.controller;

import com.project.hiptour.common.place.dto.PlaceDto;
import com.project.hiptour.common.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlace(@PathVariable("id") Integer id) {
        PlaceDto placeDto = placeService.findPlace(id);
        return ResponseEntity.ok(placeDto);
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<PlaceDto>> getRecommendedPlaces(@RequestParam(defaultValue = "10") int limit) {
        List<PlaceDto> recommendedPlaces = placeService.findRecommendedPlaces(limit);
        return ResponseEntity.ok(recommendedPlaces);
    }
}
