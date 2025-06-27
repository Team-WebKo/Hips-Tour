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
    @Operation(summary = "이미지 조회 (다운로드)")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        ImageEntity image = imageService.getById(id);
        File file = new File(image.getPath());
        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getOriginalName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}