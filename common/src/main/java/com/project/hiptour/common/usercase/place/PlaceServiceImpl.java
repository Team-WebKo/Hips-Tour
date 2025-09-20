package com.project.hiptour.common.usercase.place;

import com.project.hiptour.common.entity.place.Place;
import com.project.hiptour.common.entity.place.repos.PlaceRepository;
import com.project.hiptour.common.exception.place.PlaceNotFoundException;
import com.project.hiptour.common.web.place.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    /**
     * ID를 사용하여 특정 장소의 정보를 조회합니다.
     *
     * @param placeId 조회할 장소의 ID
     * @return 조회된 장소의 정보를 담은 PlaceDto
     * @throws PlaceNotFoundException 요청한 ID에 해당하는 장소를 찾을 수 없는 경우
     */
    @Override
    public PlaceDto findPlace(Integer placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("장소를 찾지 못했습니다: " + placeId));

        return PlaceDto.from(place);
    }

    @Override
    public Page<PlaceDto> findRecommendedPlaces(Pageable pageable) {
        return placeRepository.findAllByOrderByCreatedAtDesc(pageable).map(PlaceDto::from);
    }
}
