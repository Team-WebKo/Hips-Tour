package com.project.hiptour.common.exception;

/**
 * 리소스에 대한 접근 권한이 없을 때 발생하는 예외들의 부모 클래스 (HTTP 403 Forbidden)
 *
 * 이 예외를 상속하는 모든 자식 예외는
 * {@link com.project.hiptour.common.exception.GlobalExceptionHandler}에서 일괄 처리됩니다.
 */
public abstract class InvalidAccessException extends RuntimeException {
    public InvalidAccessException(String message) {
        super(message);
    }
}
