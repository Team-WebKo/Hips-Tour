package com.project.hiptour.common.web.place;

import com.project.hiptour.common.usercase.place.PlaceService;
import com.project.hiptour.common.util.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PageResponseDto<PlaceDto>> getRecommendedPlaces(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(placeService.findRecommendedPlaces(pageable));
    }
}
