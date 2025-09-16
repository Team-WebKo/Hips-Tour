package com.project.hiptour.common.web.place;

import com.project.hiptour.common.usercase.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/places/{placeId}")
    public ResponseEntity<PlaceDto> getPlace(@PathVariable("placeId") Integer placeId) {
        PlaceDto placeDto = placeService.findPlace(placeId);
        return ResponseEntity.ok(placeDto);
    }

    @GetMapping("/places/recommended")
    public Page<PlaceDto> getRecommendedPlaces(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return placeService.findRecommendedPlaces(pageable);
    }
}
