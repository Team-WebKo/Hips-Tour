package com.project.hiptour.common.search.serviceImpl;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.search.Service.SearchService;
import com.project.hiptour.common.search.dto.SearchResponseDto;
import com.project.hiptour.common.search.repository.SearchRepository;
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
