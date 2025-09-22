package com.project.hiptour.common.search.serviceImpl;

import com.auth0.jwt.JWT;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.users.repos.TokenRepos;
import com.project.hiptour.common.search.Service.SearchService;
import com.project.hiptour.common.search.dto.SearchResponseDto;
import com.project.hiptour.common.search.repository.SearchRepository;
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

    @Override
    public Page<SearchResponseDto> searchPlaces(String keyword, Pageable pageable) {
        return searchRepository.findByPlaceNameContaining(keyword, pageable)
                .map(SearchResponseDto::new);
    }

    @Override
    public Page<SearchResponseDto> searchHeartedPlaces(String keyword, Pageable pageable, String token) {
        // JWT에서 userId 추출
        Long userId = Long.parseLong(JWT.decode(token).getSubject());

        // 먼저 검색 결과
        Page<Place> searchResults = searchRepository.findByPlaceNameContaining(keyword, pageable);

        // 그중에서 user가 찜한 것만 필터링
        List<SearchResponseDto> heartedResults = searchResults.stream()
                .filter(place -> !heartRepos.findByUserUserIdAndFeedPlaceId(userId, place.getPlaceId()).isEmpty())
                .map(SearchResponseDto::new)
                .toList();

        return new PageImpl<>(heartedResults, pageable, heartedResults.size());
    }
}
