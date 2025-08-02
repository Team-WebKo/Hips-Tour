package com.project.hiptour.common.reviews.global.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(Long reviewId) {
        super("해당 리뷰를 찾을 수 없습니다. (리뷰 ID: " + reviewId + ")");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
