package com.project.hiptour.common.reviews.service;

import com.project.hiptour.common.place.Place;
import com.project.hiptour.common.reviews.dto.ReviewRequestDto;
import com.project.hiptour.common.reviews.dto.ReviewResponseDto;
import com.project.hiptour.common.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReviewService {
    private final ReviewRepository reviewRepository;
    //private final PlaceRepository placeRepository;
    /**
     * [SYNC] 에서 작성한 PlaceRepository 부분 사용하려면
     * sync 모듈을 주입해야 사용 가능할것으로 예상
     * 더 나은 방법 모색이 필요합니다.
     * **/

    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
    }
}
