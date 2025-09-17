package com.project.hiptour.common.exception.place;

import com.project.hiptour.common.exception.NotFoundException;

/**
 * 요청한 장소를 찾을 수 없을 때 발생하는 예외입니다.
 *
 * 이 예외는 {@link com.project.hiptour.common.exception.GlobalExceptionHandler}에서 처리
 * 되어 HTTP 404 응답을 반환합니다.
 */
public class PlaceNotFoundException extends NotFoundException {
    public PlaceNotFoundException() {
        super("해당 장소를 찾을 수 없습니다.");
    }

    public PlaceNotFoundException(String message) {
        super(message);
    }
}
