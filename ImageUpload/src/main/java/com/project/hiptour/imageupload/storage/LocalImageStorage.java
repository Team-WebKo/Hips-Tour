package com.project.hiptour.imageupload.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class LocalImageStorage implements ImageStorage {

    @Value("${file.upload-dir}")
    private final String basePath;
    private final DirectoryPartitioning directoryPartitioning;

    @Override
    public String save(MultipartFile file, String storedName) throws IOException {
        String subDir = directoryPartitioning.resolveDirectory(storedName);
        File fullDir = new File(basePath + subDir);
        if (!fullDir.exists()) fullDir.mkdirs();

        String fullPath = basePath + subDir + storedName;
        file.transferTo(new File(fullPath));
        return fullPath;
    }
    @Override
    public void delete(String fullPath) throws IOException {
        File file = new File(fullPath);
        if (file.exists()) file.delete();
    }

    @Override
    public boolean exists(String fullPath) {
        return new File(fullPath).exists();
    }

    @Override
    public void close() {
        // 리소스 정리할 필요 없음
    }


}
