package com.project.hiptour.common.reviews.global.exception;

import com.project.hiptour.common.reviews.global.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 전역 예외 처리기
     * **/
    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePlaceNotFoundException(PlaceNotFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
