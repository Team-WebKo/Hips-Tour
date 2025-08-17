package com.project.hiptour.imageupload.constants;

import java.util.List;
public class ImageValidationConstants {
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB → 매직 넘버 제거
    public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif"); // 허용 확장자
}
