package com.project.hiptour.imageupload.controller;

import com.project.hiptour.imageupload.dto.ImageResponseDto;
import com.project.hiptour.imageupload.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageResponseDto> upload(@RequestParam("file") MultipartFile file) throws Exception {
        var image = imageService.save(file);
        return ResponseEntity.ok(new ImageResponseDto(
                image.getId(),
                image.getOriginalName(),
                image.getPath()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponseDto> get(@PathVariable Long id) {
        var image = imageService.getById(id);
        return ResponseEntity.ok(new ImageResponseDto(
                image.getId(),
                image.getOriginalName(),
                image.getPath()  // 저장 경로를 URL로 사용 중인 경우
        ));
    }
}
