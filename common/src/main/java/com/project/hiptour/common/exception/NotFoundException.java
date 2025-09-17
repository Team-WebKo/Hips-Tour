package com.project.hiptour.common.exception;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외들의 부모 클래스 (HTTP 404 Not Found)
 */
public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
