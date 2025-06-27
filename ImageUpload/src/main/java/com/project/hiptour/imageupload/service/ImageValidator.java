package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.constants.ImageValidationConstants;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator {

    //파일 유효성 검사

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
    }

    //확장자 확인
    private static boolean isValidExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return false;
        String ext = fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase(); //확장자 추출
        return ImageValidationConstants.ALLOWED_EXTENSIONS.contains(ext);
    }
}
