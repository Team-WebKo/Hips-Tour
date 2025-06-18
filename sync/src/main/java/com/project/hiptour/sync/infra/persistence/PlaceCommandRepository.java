package com.project.hiptour.sync.infra.persistence;

import com.project.hiptour.common.place.Place;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceCommandRepository {
    private final PlaceRepository placeRepository;

    public PlaceCommandRepository(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void saveAll(List<Place> places) {
        placeRepository.saveAll(places);
    }

    public boolean existsByPlaceNameAndAddress1(String placeName, String address1) {
        return placeRepository.existsByPlaceNameAndAddress1(placeName, address1);
    }
}
