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
                .map(this::toDto);
    }

    private SearchResponseDto toDto(Place place) {
        return SearchResponseDto.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .address1(place.getAddress1())
                .address2(place.getAddress2())
                .tel(place.getTelNumber() != null ? place.getTelNumber().toString() : null)
                .regionName(place.getRegionInfo() != null ? place.getRegionInfo().getRegionName() : null)
                .categoryName(place.getCategory() != null ? place.getCategory().getCategoryName() : null)
                .build();
    }
}
