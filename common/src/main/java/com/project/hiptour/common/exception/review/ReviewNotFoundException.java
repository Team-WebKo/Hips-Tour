package com.project.hiptour.common.exception.review;

import com.project.hiptour.common.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException() {
        super("해당 리뷰를 찾을 수 없습니다.");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
