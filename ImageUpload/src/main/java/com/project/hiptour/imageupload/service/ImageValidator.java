package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.constants.ImageValidationConstants;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageValidator {

    // 파일 유효성 검사

    public static void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        if (!isValidExtension(file.getOriginalFilename())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }

        if (file.getSize() > ImageValidationConstants.MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 5MB를 초과합니다.");
        }
        try {
            if (!hasValidMagicNumber(file.getBytes()))
                throw new IllegalArgumentException("실제 이미지가 아닙니다.");
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 검사 중 오류가 발생했습니다.");
        }
    }

    // 확장자 확인
    public static boolean isValidExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return false;
        String ext = fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase(); //확장자 추출
        return ImageValidationConstants.ALLOWED_EXTENSIONS.contains(ext);
    }

    // 실제 이미지인지 확인
    public static boolean hasValidMagicNumber(byte[] bytes) {
        return isJpeg(bytes) || isPng(bytes) || isGif(bytes);
    }


    // JPEG
    private static boolean isJpeg(byte[] bytes) {
        return bytes.length >= 3 &&
                (bytes[0] & 0xFF) == 0xFF &&
                (bytes[1] & 0xFF) == 0xD8 &&
                (bytes[2] & 0xFF) == 0xFF;
    }

    // PNG
    private static boolean isPng(byte[] bytes) {
        return bytes.length >= 8 &&
                (bytes[0] & 0xFF) == 0x89 &&
                (char) bytes[1] == 'P' &&
                (char) bytes[2] == 'N' &&
                (char) bytes[3] == 'G';
    }

    // GIF
    public static boolean isGif(byte[] bytes) {
        return bytes.length >= 6 &&
                (char) bytes[0] == 'G' &&
                (char) bytes[1] == 'I' &&
                (char) bytes[2] == 'F';
    }
}
