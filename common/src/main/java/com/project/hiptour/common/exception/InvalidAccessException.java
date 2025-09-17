package com.project.hiptour.common.exception;

/**
 * 리소스에 대한 접근 권한이 없을 때 발생하는 예외들의 부모 클래스 (HTTP 403 Forbidden)
 */
public abstract class InvalidAccessException extends RuntimeException {
    public InvalidAccessException(String message) {
        super(message);
    }
}
