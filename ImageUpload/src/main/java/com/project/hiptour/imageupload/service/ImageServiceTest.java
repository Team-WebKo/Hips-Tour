package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class ImageServiceTest {

    private ImageRepository imageRepository;
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageRepository = mock(ImageRepository.class);
        imageService = new ImageService(imageRepository);
        imageService.setUploadDir("uploads-test/");
    }


    @Test
    void 이미지_저장() throws IOException {

        String originalName = "test.jpg";
        String contentType = "image/jpeg";
        byte[] content = "abc123".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", originalName, contentType, content);

        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(invocation -> {
            ImageEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(LocalDateTime.now());
            return saved;
        });

        ImageEntity saved = imageService.save(mockFile);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOriginalName()).isEqualTo(originalName);
        assertThat(saved.getStoredName()).contains(originalName);
        assertThat(saved.getPath()).contains("uploads-test/");
        assertThat(new File(saved.getPath()).exists()).isTrue();
    }
}
