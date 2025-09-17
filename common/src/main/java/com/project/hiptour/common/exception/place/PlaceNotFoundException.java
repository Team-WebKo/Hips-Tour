package com.project.hiptour.common.exception.place;

import com.project.hiptour.common.exception.NotFoundException;

public class PlaceNotFoundException extends NotFoundException {
    public PlaceNotFoundException() {
        super("해당 장소를 찾을 수 없습니다.");
    }

    public PlaceNotFoundException(String message) {
        super(message);
    }
}
