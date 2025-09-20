package com.project.hiptour.common.exception.review;

import com.project.hiptour.common.exception.InvalidAccessException;

/**
 * 리뷰에 대한 접근 권한이 없을 때 발생하는 예외입니다.
 *
 * 이 예외는 {@link com.project.hiptour.common.exception.GlobalExceptionHandler}에서 처리
 * 되어 HTTP 403 응답을 반환합니다.
 */
public class ReviewAccessDeniedException extends InvalidAccessException {
    public ReviewAccessDeniedException() {
        super("해당 리뷰에 대한 접근 권한이 없습니다.");
    }

    public ReviewAccessDeniedException(String message) {
        super(message);
    }
}
