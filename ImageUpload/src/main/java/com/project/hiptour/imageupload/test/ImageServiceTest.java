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

        String originalName = "test.jpg";
        byte[] content = "abc123".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", originalName, "image/jpeg", content);

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

    @Test
    void 이미지_저장_실패시_파일_삭제() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "fail.jpg", "image/jpeg", "fail".getBytes());

        ImageStorage mockFileManager = mock(ImageStorage.class);

        imageService = new ImageService(imageRepository, mockFileManager);

        RuntimeException e = assertThrows(RuntimeException.class, () -> imageService.save(mockFile));
        assertThat(e.getMessage()).isEqualTo("파일 업로드에 실패했습니다. 다시 시도해주세요.");
    }
}
