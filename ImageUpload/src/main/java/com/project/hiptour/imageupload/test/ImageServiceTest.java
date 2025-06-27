package com.project.hiptour.imageupload.test;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import com.project.hiptour.imageupload.service.ImageService;
import com.project.hiptour.imageupload.storage.DataBasedDirectoryPartitioning;
import com.project.hiptour.imageupload.storage.ImageStorage;
import com.project.hiptour.imageupload.storage.LocalImageStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class ImageServiceTest {

    private ImageRepository imageRepository;
    private ImageStorage imageStorage;
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageRepository = mock(ImageRepository.class);
        imageStorage = new LocalImageStorage("uploads-test/", new DataBasedDirectoryPartitioning());
        imageService = new ImageService(imageRepository, imageStorage);
    }


    @Test
    void 이미지_저장() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "abc123".getBytes());

        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(invocation -> {
            ImageEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(LocalDateTime.now());
            return saved;
        });

        ImageEntity saved = imageService.save(file);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOriginalName()).isEqualTo("test.jpg");
    }

    //예외처리

    @Test
    void 비어있는_파일() {
        MockMultipartFile file = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> imageService.save(file));
    }

    @Test
    void 허용되지_않는_확장자() {
        MockMultipartFile file = new MockMultipartFile("file", "virus.exe", "application/octet-stream", "boom".getBytes());
        assertThrows(IllegalArgumentException.class, () -> imageService.save(file));
    }

    @Test
    void 파일_크기_초과() {
        byte[] tooBig = new byte[6 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile("file", "huge.jpg", "image/jpeg", tooBig);
        assertThrows(IllegalArgumentException.class, () -> imageService.save(file));
    }
}
