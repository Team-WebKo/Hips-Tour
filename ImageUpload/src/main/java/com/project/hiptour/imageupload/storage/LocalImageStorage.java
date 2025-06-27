package com.project.hiptour.imageupload.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Component
public class LocalImageStorage implements ImageStorage {

    @Value("${file.upload-dir}")
    private String basePath;

    private final DirectoryPartitioning directoryPartitioning;

    public  LocalImageStorage(DirectoryPartitioning directoryPartitioning){
        this.directoryPartitioning = directoryPartitioning;
    }

    @Override
    public String save(MultipartFile file, String storedName) throws IOException{
        return save(file.getBytes(), storedName);
    }
    @Override
    public String save(byte[] bytes, String storedName) throws IOException {
        String subDir = directoryPartitioning.resolveDirectory(storedName);
        File dir = new File(basePath + subDir);
        if (!dir.exists()) dir.mkdirs();

        String fullPath = basePath + subDir + storedName;
        Files.write(Paths.get(fullPath), bytes);
        return fullPath;
    }
    @Override
    public void delete(String fullPath) throws IOException {
        new File(fullPath).delete();
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
