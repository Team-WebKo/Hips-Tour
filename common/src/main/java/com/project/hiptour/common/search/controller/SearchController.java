package com.project.hiptour.common.search.controller;

import com.project.hiptour.common.entity.users.TokenInfo;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.search.Service.SearchService;
import com.project.hiptour.common.search.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping("/search")
    public ResponseEntity<Page<SearchResponseDto>> searchPlaces(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "placeName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(searchService.searchPlaces(keyword, pageable));
    }

    // 찜 필터링 검색 API
    @GetMapping("/search/hearted")
    public ResponseEntity<Page<SearchResponseDto>> searchHeartedPlaces(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "placeName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestHeader("Authorization") String authHeader
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String token = authHeader.replace("Bearer ", "");

        return ResponseEntity.ok(searchService.searchHeartedPlaces(keyword, pageable, token));
    }
}
