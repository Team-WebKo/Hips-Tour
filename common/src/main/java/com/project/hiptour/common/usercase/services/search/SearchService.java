package com.project.hiptour.common.usercase.services.search;

import com.project.hiptour.common.web.search.SearchResponseDto;
import com.project.hiptour.common.util.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    PageResponseDto<SearchResponseDto> searchPlaces(String keyword, Pageable pageable);
    PageResponseDto<SearchResponseDto> searchHeartedPlaces(String keyword, Pageable pageable, String token);
}