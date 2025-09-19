package com.project.hiptour.sync.infrastructure.api;

public class TourApiCommunicationException extends RuntimeException{
    public TourApiCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
