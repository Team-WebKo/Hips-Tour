package com.project.hiptour.common.exception.review;

import com.project.hiptour.common.exception.NotFoundException;

/**
 * 요청한 리뷰를 찾을 수 없을 때 발생하는 예외입니다.
 *
 * 이 예외는 {@link com.project.hiptour.common.exception.GlobalExceptionHandler}에서 처리
 * 되어 HTTP 404 응답을 반환합니다.
 */
public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException() {
        super("해당 리뷰를 찾을 수 없습니다.");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
