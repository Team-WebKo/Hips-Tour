package com.project.hiptour.imageupload.service;

import com.project.hiptour.imageupload.entity.ImageEntity;
import com.project.hiptour.imageupload.respository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter

public class ImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;

    public ImageEntity save(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String storedName = uuid + "_" + file.getOriginalFilename();

        File dir = new File(uploadDir);
        //디렉토리 없으면 생성(없으면 저장시 실패 할 수 있음)
        if (!dir.exists()) dir.mkdirs();

        String path = uploadDir + storedName;
        file.transferTo(new File(path));

        ImageEntity image = new ImageEntity();
        image.setOriginalName(file.getOriginalFilename());
        image.setStoredName(storedName);
        image.setPath(path);
        image.setCreatedAt(LocalDateTime.now());

        return imageRepository.save(image);
    }

    public ImageEntity getById(Long id) {
        return imageRepository.findById(id).orElseThrow();
    }
}


