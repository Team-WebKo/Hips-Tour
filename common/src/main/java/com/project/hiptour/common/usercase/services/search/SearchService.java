package com.project.hiptour.common.usercase.services.search;

import com.project.hiptour.common.web.search.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<SearchResponseDto> searchPlaces(String keyword, Pageable pageable);
}
