package com.project.hiptour.imageupload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageResponseDto {
    private Long id;
    private String originalName;
    private String url;
}
