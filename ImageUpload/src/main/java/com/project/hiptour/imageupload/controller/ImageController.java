package com.project.hiptour.imageupload.controller;

import com.project.hiptour.imageupload.dto.ImageResponseDto;
import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/image-upload/images")
@Tag(name = "Image", description = "이미지 업로드 API")
public class ImageController {
    private final ImageService imageService;

    @Value("${image.base-url}")
    private String baseUrl;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ImageResponseDto> upload(
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestParam("file") MultipartFile file) {
        ImageEntity image = imageService.save(file);

        ImageResponseDto response = new ImageResponseDto(
                image.getId(),
                image.getOriginalName(),
                baseUrl +  image.getPath()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "이미지 정보 반환")
    public ResponseEntity<ImageResponseDto> getImageUrl(@PathVariable Long id) {
        ImageEntity image = imageService.getById(id);

        ImageResponseDto response = new ImageResponseDto(
                image.getId(),
                image.getOriginalName(),
                baseUrl + image.getPath()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}