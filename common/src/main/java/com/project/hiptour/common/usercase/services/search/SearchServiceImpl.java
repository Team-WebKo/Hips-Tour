package com.project.hiptour.common.usercase.services.search;

import com.project.hiptour.common.web.search.SearchResponseDto;
import com.project.hiptour.common.entity.place.repos.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;

    @Override
    public Page<SearchResponseDto> searchPlaces(String keyword, Pageable pageable) {
        return searchRepository.findByPlaceNameContaining(keyword, pageable)
                .map(SearchResponseDto::new);
    }
}
