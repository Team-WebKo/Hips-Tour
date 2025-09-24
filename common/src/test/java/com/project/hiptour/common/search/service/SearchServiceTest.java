package com.project.hiptour.common.search.service;
import com.project.hiptour.common.entity.heart.Heart;
import com.project.hiptour.common.entity.place.repos.SearchRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.hiptour.common.entity.heart.repos.HeartRepos;
import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.usercase.services.search.SearchService;
import com.project.hiptour.common.usercase.services.search.SearchServiceImpl;
import com.project.hiptour.common.util.PageResponseDto;
import com.project.hiptour.common.web.search.SearchResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SearchServiceImplTest {

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private HeartRepos heartRepos;

    @InjectMocks
    private SearchServiceImpl searchService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pageable = PageRequest.of(0, 10);
    }

    @DisplayName("검색: 키워드로 장소 검색 성공")
    @Test
    void success() {
        // given
        String keyword = "부산";

        Place place1 = new Place("부산 해운대", "부산광역시 해운대구", null, null, null);
        place1.setPlaceId(1); // 직접 세팅
        Place place2 = new Place("부산 광안리", "부산광역시 수영구", null, null, null);
        place2.setPlaceId(2);

        List<Place> places = Arrays.asList(place1, place2);
        Page<Place> placePage = new PageImpl<>(places, pageable, places.size());

        when(searchRepository.findByPlaceNameContaining(eq(keyword), any(Pageable.class)))
                .thenReturn(placePage);

        // when
        PageResponseDto<SearchResponseDto> result = searchService.searchPlaces(keyword, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getPlaceName()).contains("부산");
        verify(searchRepository, times(1)).findByPlaceNameContaining(eq(keyword), any(Pageable.class));
    }

    @DisplayName("찜 검색: 유저가 찜한 장소만 필터링")
    @Test
    void Hearted_success() {
        // given
        String keyword = "부산";
        String token = JWT.create().withSubject("1").sign(Algorithm.HMAC256("test"));

        Place place1 = new Place("부산 해운대", "부산광역시 해운대구", null, null, null);
        place1.setPlaceId(1);
        Place place2 = new Place("부산 광안리", "부산광역시 수영구", null, null, null);
        place2.setPlaceId(2);

        List<Place> places = Arrays.asList(place1, place2);
        Page<Place> placePage = new PageImpl<>(places, pageable, places.size());

        when(searchRepository.findByPlaceNameContaining(eq(keyword), any(Pageable.class)))
                .thenReturn(placePage);

        // place1은 찜 O, place2는 찜 X
        Heart mockHeart = new Heart();
        when(heartRepos.findByUserUserIdAndFeedPlaceId(1L, 1))
                .thenReturn(List.of(mockHeart));
        when(heartRepos.findByUserUserIdAndFeedPlaceId(1L, 2)).thenReturn(List.of());

        // when
        PageResponseDto<SearchResponseDto> result = searchService.searchHeartedPlaces(keyword, pageable, token);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPlaceName()).isEqualTo("부산 해운대");
        verify(searchRepository, times(1)).findByPlaceNameContaining(eq(keyword), any(Pageable.class));
        verify(heartRepos, times(1)).findByUserUserIdAndFeedPlaceId(1L, 1);
        verify(heartRepos, times(1)).findByUserUserIdAndFeedPlaceId(1L, 2);
    }

    @DisplayName("찜 검색: 결과가 없을 경우 빈 페이지 반환")
    @Test
    void emptyResult() {
        // given
        String keyword = "서울";
        String token = JWT.create().withSubject("1").sign(Algorithm.HMAC256("test"));

        Place place = new Place("서울 남산타워", "서울 중구", null, null, null);
        place.setPlaceId(10);

        List<Place> places = List.of(place);
        Page<Place> placePage = new PageImpl<>(places, pageable, places.size());

        when(searchRepository.findByPlaceNameContaining(eq(keyword), any(Pageable.class)))
                .thenReturn(placePage);

        // 찜한 게 없음
        when(heartRepos.findByUserUserIdAndFeedPlaceId(1L, 10)).thenReturn(List.of());

        // when
        PageResponseDto<SearchResponseDto> result = searchService.searchHeartedPlaces(keyword, pageable, token);

        // then
        assertThat(result.getContent()).isEmpty();
        verify(searchRepository, times(1)).findByPlaceNameContaining(eq(keyword), any(Pageable.class));
        verify(heartRepos, times(1)).findByUserUserIdAndFeedPlaceId(1L, 10);
    }
}