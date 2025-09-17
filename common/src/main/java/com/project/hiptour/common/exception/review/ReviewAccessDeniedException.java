package com.project.hiptour.common.exception.review;

import com.project.hiptour.common.exception.InvalidAccessException;

public class ReviewAccessDeniedException extends InvalidAccessException {
    public ReviewAccessDeniedException() {
        super("해당 리뷰에 대한 접근 권한이 없습니다.");
    }

    public ReviewAccessDeniedException(String message) {
        super(message);
    }
}
