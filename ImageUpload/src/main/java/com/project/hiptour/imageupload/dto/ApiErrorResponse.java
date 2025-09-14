package com.project.hiptour.imageupload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
}
