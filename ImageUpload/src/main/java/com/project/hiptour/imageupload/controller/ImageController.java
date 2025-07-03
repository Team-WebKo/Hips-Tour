package com.project.hiptour.imageupload.controller;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/images")
@Tag(name = "Image", description = "이미지 업로드 API")
public class ImageController {
    private final ImageService imageService;
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @Operation(summary = "이미지 업로드")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        ImageEntity image = imageService.save(file);
        return ResponseEntity.ok("/images/" + image.getId());
    }

    @GetMapping("/{id}")
    @Operation(summary = "이미지 URL 반환")
    public ResponseEntity<String> getImageUrl(@PathVariable Long id) {
        // 실제 경로는 nginx 기준에 맞게 조정
        return ResponseEntity.ok("http://localhost:8080/images/raw/" + id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}