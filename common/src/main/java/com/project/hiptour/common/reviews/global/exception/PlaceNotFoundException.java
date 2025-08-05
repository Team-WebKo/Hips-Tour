package com.project.hiptour.common.reviews.global.exception;

public class PlaceNotFoundException extends RuntimeException {
    public PlaceNotFoundException(Long placeId) {
        super("해당 장소(placeId=" + placeId + ")가 존재하지 않습니다.");
    }
}
