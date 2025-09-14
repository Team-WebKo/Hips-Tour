package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import com.project.hiptour.imageupload.storage.DataBasedDirectoryPartitioning;
import com.project.hiptour.imageupload.storage.ImageStorage;
import com.project.hiptour.imageupload.storage.LocalImageStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
        imageStorage = new LocalImageStorage( new DataBasedDirectoryPartitioning());
        imageService = new ImageService(imageRepository, imageStorage);
    }


    // 테스트용 이미지 생성 메서드 (jpg/png 등 원하는 포맷 지정 가능)
    private byte[] createTestImage(String format) throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    @Test
    void 이미지_저장() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", createTestImage("jpg"));

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

    @Test
    void PNG_업로드_시_JPG로_변환되어_저장() throws IOException {
        MockMultipartFile pngFile = new MockMultipartFile("file", "sample.png", "image/png", createTestImage("png"));

        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(invocation -> {
            ImageEntity saved = invocation.getArgument(0);
            saved.setId(123L);
            saved.setCreatedAt(LocalDateTime.now());
            return saved;
        });

        ImageEntity result = imageService.save(pngFile);

        assertThat(result.getStoredName()).endsWith(".jpg"); // jpg 변환확인
    }

    @Test
    void 이미지_삭제시_deletedAt() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "delete.jpg", "image/jpeg", createTestImage("jpg"));

        when(imageRepository.save(any(ImageEntity.class))).thenAnswer(invocation -> {
            ImageEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setCreatedAt(LocalDateTime.now());
            return saved;
        });

        ImageEntity saved = imageService.save(file);

        when(imageRepository.findById(saved.getId())).thenReturn(java.util.Optional.of(saved));

        imageService.delete(saved.getId());

        assertThat(saved.getDeletedAt()).isNotNull();
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
