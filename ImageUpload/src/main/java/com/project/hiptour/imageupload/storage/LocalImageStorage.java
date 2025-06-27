package com.project.hiptour.imageupload.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
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
        if (!dir.exists()){
            boolean created = dir.mkdirs();
            if (created) {
                log.info("디렉토리 생성: {}", dir.getAbsolutePath());
            }
        }

        String fullPath = basePath + subDir + storedName;
        Files.write(Paths.get(fullPath), bytes);
        log.info("로컬 저장: {}", fullPath);
        return fullPath;
    }
    @Override
    public void delete(String fullPath) throws IOException {
        boolean deleted = new File(fullPath).delete();
        if (deleted) {
            log.info("파일 삭제 성공: {}", fullPath);
        } else {
            log.warn("파일 삭제 실패: {}", fullPath);
        }
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
