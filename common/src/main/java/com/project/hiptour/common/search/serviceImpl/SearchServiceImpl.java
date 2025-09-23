package com.project.hiptour.common.search.serviceImpl;

import com.auth0.jwt.JWT;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.search.Service.SearchService;
import com.project.hiptour.common.search.dto.SearchResponseDto;
import com.project.hiptour.common.search.repository.SearchRepository;
import com.project.hiptour.common.util.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;
    private final HeartRepos heartRepos;

    public PageResponseDto<SearchResponseDto> searchPlaces(String keyword, Pageable pageable) {
        Page<Place> searchResults = searchRepository.findByPlaceNameContaining(keyword, pageable);
        return PageResponseDto.fromPage(searchResults, SearchResponseDto::new);
    }


    @Override
    public PageResponseDto<SearchResponseDto> searchHeartedPlaces(String keyword, Pageable pageable, String token) {
        // JWT에서 userId 추출
        Long userId = Long.parseLong(JWT.decode(token).getSubject());

        // 먼저 검색 결과
        Page<Place> searchResults = searchRepository.findByPlaceNameContaining(keyword, pageable);

        // 그중에서 user가 찜한 것만 필터링
        List<Place> filtered = searchResults.stream()
                .filter(place -> !heartRepos.findByUserUserIdAndFeedPlaceId(userId, place.getPlaceId()).isEmpty())
                .toList();

        Page<SearchResponseDto> heartedPage =
                new PageImpl<>(filtered.stream().map(SearchResponseDto::new).toList(), pageable, filtered.size());

        return PageResponseDto.fromPage(heartedPage, dto -> dto);
    }
}
