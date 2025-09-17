package com.project.hiptour.common.exception;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외들의 부모 클래스 (HTTP 404 Not Found)
 *
 * 이 예외를 상속하는 모든 자식 예외는
 * {@link com.project.hiptour.common.exception.GlobalExceptionHandler}에서 일괄 처리됩니다.
 */
public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
