package com.project.hiptour.common.search.Service;

import com.project.hiptour.common.search.dto.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<SearchResponseDto> searchPlaces(String keyword, Pageable pageable);
    Page<SearchResponseDto> searchHeartedPlaces(String keyword, Pageable pageable, String token);
}