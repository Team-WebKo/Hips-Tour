package com.project.hiptour.common.exception;

/**
 * 요청한 유저를 찾을 수 없을 때 발생하는 예외입니다.
 * 이 예외는 {@link GlobalExceptionHandler}에서 처리되어 HTTP 404 응답을 반환합니다.
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("해당 유저를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
