package com.project.hiptour.imageupload.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ImageValidaterTest {
    @Test
    void 정상적인_JPEG_매직넘버는_통과() {
        byte[] jpeg = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
        assertTrue(ImageValidator.hasValidMagicNumber(jpeg));
    }

    @Test
    void 정상적인_PNG_매직넘버는_통과() {
        byte[] png = new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};
        assertTrue(ImageValidator.hasValidMagicNumber(png));
    }

    @Test
    void 정상적인_GIF_매직넘버는_통과() {
        byte[] gif = new byte[]{'G', 'I', 'F', '8', '9', 'a'};
        assertTrue(ImageValidator.hasValidMagicNumber(gif));
    }

    @Test
    void 실행파일_매직넘버_거부() { // (예시 exe)
        byte[] exe = new byte[]{'M', 'Z', 0x00, 0x00};
        assertFalse(ImageValidator.hasValidMagicNumber(exe));
    }

    @Test
    void 확장자만_이미지인_위장파일_거절() {
        byte[] fakeImage = new byte[]{'M', 'Z'}; // exe 매직넘버
        MockMultipartFile file = new MockMultipartFile("file", "hacked.jpg", "image/jpeg", fakeImage);
        Exception e = assertThrows(IllegalArgumentException.class, () -> ImageValidator.validate(file));
        assertEquals("유효하지 않은 이미지 포맷입니다.", e.getMessage());
    }

    @Test
    void 빈_파일_거부() {
        MockMultipartFile empty = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);
        Exception e = assertThrows(IllegalArgumentException.class, () -> ImageValidator.validate(empty));
        assertEquals("유효하지 않은 이미지 포맷입니다.", e.getMessage());
    }
}
